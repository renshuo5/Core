package com.rensframework.core.spring;

import java.beans.PropertyEditorSupport;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

public class PageableArgumentResolver
  implements WebArgumentResolver
{
  private static final Pageable DEFAULT_PAGE_REQUEST = new PageRequest(0, 10);
  private static final String DEFAULT_PREFIX = "page";
  private static final String DEFAULT_SEPARATOR = ".";
  private Pageable fallbackPagable;
  private String prefix;
  private String separator;
  protected final transient Logger logger;
  private static final String PAGE_SIZE_COOKIE_KEY = "page.size";
  public static final String PARAM_NOT_STORE = "page.transient";
  private static final int PAGE_SIZE_COOKIE_MAX_AGE = 2592000;
  private static final int PAGE_SIZE_LIMIT = 500;
  
  public void setFallbackPagable(Pageable fallbackPagable)
  {
    this.fallbackPagable = (null == fallbackPagable ? DEFAULT_PAGE_REQUEST : fallbackPagable);
  }
  
  public void setPrefix(String prefix)
  {
    this.prefix = (null == prefix ? "page" : prefix);
  }
  
  public void setSeparator(String separator)
  {
    this.separator = (null == separator ? "." : separator);
  }
  
  public PageableArgumentResolver()
  {
    this.fallbackPagable = DEFAULT_PAGE_REQUEST;
    this.prefix = "page";
    this.separator = ".";
    
    this.logger = LoggerFactory.getLogger(getClass());
  }
  
  public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest)
  {
    if (methodParameter.getParameterType().equals(Pageable.class))
    {
      assertPageableUniqueness(methodParameter);
      
      Pageable request = getDefaultFromAnnotationOrFallback(methodParameter);
      int cookiePageSize = getPageSizeFromCookie(
        (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class));
      if (cookiePageSize != 0) {
        request = new PageRequest(request.getPageNumber(), cookiePageSize, request.getSort());
      }
      int defaultPageSize = request.getPageSize();
      
      ServletRequest servletRequest = (ServletRequest)webRequest.getNativeRequest();
      
      PropertyValues propertyValues = new ServletRequestParameterPropertyValues(servletRequest, getPrefix(methodParameter), this.separator);
      
      DataBinder binder = new ServletRequestDataBinder(request);
      
      binder.initDirectFieldAccess();
      binder.registerCustomEditor(Sort.class, new SortPropertyEditor("sort.dir", propertyValues));
      
      binder.bind(propertyValues);
      if (request.getPageSize() > 500) {
        throw new IllegalArgumentException("���������������" + request.getPageSize() + "������������" + 500);
      }
      if ((request.getPageSize() != defaultPageSize) && 
        (!"true".equals(webRequest.getParameter("page.transient")))) {
        setPageSizeToCookie(
          (HttpServletResponse)webRequest.getNativeResponse(HttpServletResponse.class), request
          .getPageSize());
      }
      if (request.getPageNumber() > 0) {
        request = new PageRequest(request.getPageNumber() - 1, request.getPageSize(), request.getSort());
      }
      return request;
    }
    return UNRESOLVED;
  }
  
  private int getPageSizeFromCookie(HttpServletRequest req)
  {
    Cookie[] cs = req.getCookies();
    if (cs == null) {
      return 0;
    }
    for (Cookie c : cs) {
      if ("page.size".equalsIgnoreCase(c.getName())) {
        try
        {
          int size = Integer.parseInt(c.getValue());
          if ((size > 0) && (size <= 500)) {
            return size;
          }
          return 0;
        }
        catch (NumberFormatException e)
        {
          this.logger.warn("read page.size from cookie failure, error info: ", e);
          
          return 0;
        }
      }
    }
    return 0;
  }
  
  private void setPageSizeToCookie(HttpServletResponse res, int pageSize)
  {
    Cookie cookie = new Cookie("page.size", String.valueOf(pageSize));
    cookie.setMaxAge(2592000);
    cookie.setPath("/");
    res.addCookie(cookie);
  }
  
  private Pageable getDefaultFromAnnotationOrFallback(MethodParameter methodParameter)
  {
    for (Annotation annotation : methodParameter.getParameterAnnotations()) {
      if ((annotation instanceof PageableDefault)) {
        return getDefaultPageRequestFrom((PageableDefault)annotation);
      }
    }
    return new PageRequest(this.fallbackPagable.getPageNumber(), this.fallbackPagable.getPageSize(), this.fallbackPagable.getSort());
  }
  
  private static Pageable getDefaultPageRequestFrom(PageableDefault defaults)
  {
    int defaultPageNumber = defaults.page() + 1;
    int defaultPageSize = defaults.value();
    if (defaults.sort().length == 0) {
      return new PageRequest(defaultPageNumber, defaultPageSize);
    }
    return new PageRequest(defaultPageNumber, defaultPageSize, defaults.direction(), defaults.sort());
  }
  
  private String getPrefix(MethodParameter parameter)
  {
    for (Annotation annotation : parameter.getParameterAnnotations()) {
      if ((annotation instanceof Qualifier)) {
        return ((Qualifier)annotation).value() + "_" + this.prefix;
      }
    }
    return this.prefix;
  }
  
  private void assertPageableUniqueness(MethodParameter parameter)
  {
    Method method = parameter.getMethod();
    if (containsMoreThanOnePageableParameter(method))
    {
      Annotation[][] annotations = method.getParameterAnnotations();
      assertQualifiersFor(method.getParameterTypes(), annotations);
    }
  }
  
  private boolean containsMoreThanOnePageableParameter(Method method)
  {
    boolean pageableFound = false;
    for (Class<?> type : method.getParameterTypes())
    {
      if ((pageableFound) && (type.equals(Pageable.class))) {
        return true;
      }
      if (type.equals(Pageable.class)) {
        pageableFound = true;
      }
    }
    return false;
  }
  
  private void assertQualifiersFor(Class<?>[] parameterTypes, Annotation[][] annotations)
  {
    Set<String> values = new HashSet();
    for (int i = 0; i < annotations.length; i++) {
      if (Pageable.class.equals(parameterTypes[i]))
      {
        Qualifier qualifier = findAnnotation(annotations[i]);
        if (null == qualifier) {
          throw new IllegalStateException("Ambiguous Pageable arguments in handler method. If you use multiple parameters of type Pageable you need to qualify them with @Qualifier");
        }
        if (values.contains(qualifier.value())) {
          throw new IllegalStateException("Values of the user Qualifiers must be unique!");
        }
        values.add(qualifier.value());
      }
    }
  }
  
  private Qualifier findAnnotation(Annotation[] annotations)
  {
    for (Annotation annotation : annotations) {
      if ((annotation instanceof Qualifier)) {
        return (Qualifier)annotation;
      }
    }
    return null;
  }
  
  private static class SortPropertyEditor
    extends PropertyEditorSupport
  {
    private final String orderProperty;
    private final PropertyValues values;
    
    public SortPropertyEditor(String orderProperty, PropertyValues values)
    {
      this.orderProperty = orderProperty;
      this.values = values;
    }
    
    public void setAsText(String text)
    {
      PropertyValue rawOrder = this.values.getPropertyValue(this.orderProperty);
      
      Sort.Direction order = null == rawOrder ? Sort.Direction.ASC : Sort.Direction.fromString(rawOrder.getValue().toString());
      
      setValue(new Sort(order, new String[] { text }));
    }
  }
}
