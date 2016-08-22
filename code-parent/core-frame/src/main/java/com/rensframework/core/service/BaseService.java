package com.rensframework.core.service;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.BigIntegerConverter;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.beanutils.converters.SqlTimeConverter;
import org.apache.commons.beanutils.converters.SqlTimestampConverter;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.hibernate.mapping.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.rensframework.core.entity.Auditable;
import com.rensframework.core.entity.Entity;

@Transactional(readOnly = true)
public abstract class BaseService<T extends Entity> {

	protected final transient Logger logger = LoggerFactory
			.getLogger(getClass());

	static {
		ConvertUtils.register(new DateConverter(null), java.util.Date.class);
		ConvertUtils.register(new SqlDateConverter(null), java.sql.Date.class);
		ConvertUtils.register(new SqlTimeConverter(null), Time.class);
		ConvertUtils.register(new SqlTimestampConverter(null), Timestamp.class);

		ConvertUtils.register(new BooleanConverter(null), Boolean.class);
		ConvertUtils.register(new ShortConverter(null), Short.class);
		ConvertUtils.register(new IntegerConverter(null), Integer.class);
		ConvertUtils.register(new LongConverter(null), Long.class);
		ConvertUtils.register(new FloatConverter(null), Float.class);
		ConvertUtils.register(new DoubleConverter(null), Double.class);
		ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
		ConvertUtils.register(new BigIntegerConverter(null), BigInteger.class);
	}

	protected abstract PagingAndSortingRepository<T, Long> getDao();

	public T getForUpdate(Long id) {
		T t = (T) getDao().findOne(id);
		if (t instanceof Auditable) {
			try {
				T orig = (T) BeanUtils.cloneBean(t);
				((Auditable) t).setOrig(orig);
				copyComponentProps(orig, t);
			} catch (Exception e) {
				this.logger.error(ExceptionUtils.getStackTrace(e));
				throw new RuntimeException(e);
			}
		}
		return t;
	}

	private void copyComponentProps(T copy, T orig) {
		PropertyDescriptor[] descriptors = BeanUtilsBean.getInstance()
				.getPropertyUtils().getPropertyDescriptors(orig.getClass());
		for (PropertyDescriptor pd : descriptors) {
			if (Component.class.isAssignableFrom(pd.getPropertyType())) {
				Method getter = pd.getReadMethod();
				Method setter = pd.getWriteMethod();
				try {
					Object value = getter.invoke(orig, new Object[0]);
					if (null == value) {
						setter.invoke(copy, new Object[] { null });
					} else {
						Object copyValue = value.getClass().newInstance();
						org.springframework.beans.BeanUtils.copyProperties(
								value, copyValue);

						setter.invoke(copy, new Object[] { copyValue });
					}
				} catch (Exception e) {
					this.logger.error(ExceptionUtils.getStackTrace(e));
					throw new IllegalArgumentException();
				}
			}
		}
	}

	@Transactional(readOnly = false)
	public T saveWithAudit(T t) {
		// 可添加日志操作信息
		getDao().save(t);
		return t;
	}

	@Transactional(readOnly = false)
	public T save(T entity) {
		return (T) getDao().save(entity);
	}

	@Transactional(readOnly = false)
	public void deleteWithAudit(T t) {
		getDao().delete(t);
	}

	@Transactional(readOnly = false)
	public void deleteWithAudit(Long id) {
		T t = (T) getDao().findOne(id);
		deleteWithAudit(t);
	}

	@Transactional(readOnly = false)
	public void deleteWithAudit(Long[] ids) {
		for (Long l : ids) {
			deleteWithAudit(l);
		}
	}

	@Transactional(readOnly = false)
	public void deleteWithAudit(Iterable<? extends T> entities) {
		for (T entity : entities) {
			deleteWithAudit(entity);
		}
	}

	@Transactional(readOnly = false)
	public <S extends T> Iterable<S> save(Iterable<S> entities) {
		return getDao().save(entities);
	}

	public T findOne(Long id) {
		return (T) getDao().findOne(id);
	}

	public Iterable<T> findAll() {
		return getDao().findAll();
	}

	public Iterable<T> findAll(Iterable<Long> ids) {
		return getDao().findAll(ids);
	}

	public Iterable<T> findAll(Sort sort) {
		return getDao().findAll(sort);
	}

	public Page<T> findAll(Pageable pageable) {
		return getDao().findAll(pageable);
	}

	public Long count() {
		return Long.valueOf(getDao().count());
	}

	public boolean exists(Long id) {
		return getDao().exists(id);
	}

}
