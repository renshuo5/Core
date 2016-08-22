//package com.rs.user.controller;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.subject.Subject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.FieldError;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.ServletRequestDataBinder;
//import org.springframework.web.bind.annotation.InitBinder;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.model.entity.Role;
//import com.model.entity.User;
//import com.rs.user.model.LoginInfo;
//import com.rs.user.service.RoleService;
//import com.rs.user.service.UserService;
//
//@Controller
//@RequestMapping({ "/user/" })
//public class UserController {
//	protected final transient Logger logger = LoggerFactory
//			.getLogger(getClass());
//	@Resource
//	private UserService userService;
//	@Resource
//	private RoleService roleService;
//
//	@InitBinder({ "entity" })
//	private void initBinder(ServletRequestDataBinder binder,
//			HttpServletRequest req) {
//		List<String> fields = new ArrayList(Arrays.asList(new String[] {
//				"encryptPassword", "name", "email", "cellphone", "telephone",
//				"rolesForPage", "active" }));
//		if (SecurityUtils.getSubject().isPermitted("operation:userAdmin")) {
//			fields.add("audited");
//			fields.add("removed");
//		}
//
//		switch (this.getMethod(req)) {
//		case POST:
//			fields.add("account");
//			fields.add("poolId");
//			binder.setAllowedFields((String[]) fields.toArray(new String[0]));
//		case PUT:
//			binder.setAllowedFields((String[]) fields.toArray(new String[0]));
//			break;
//		}
//	}
//
//	@ModelAttribute("entity")
//	public User prepare(@RequestParam(value = "id", required = false) Long id,
//			HttpServletRequest req) {
//		RequestMethod method = this.getMethod(req);
//		if ((id != null) && (id.longValue() > 0L)
//				&& (RequestMethod.PUT.equals(method))) {
//			return (User) this.userService.getForUpdate(id.longValue());
//		}
//		if (RequestMethod.POST.equals(method)) {
//			return new User();
//		}
//		return null;
//	}
//
//	@RequestMapping(value = { "/form" }, method = { RequestMethod.GET })
//	public String form(@RequestParam(value = "id", required = false) User user,
//			@RequestParam(value = "poolId", required = false) Long poolId,
//			Model model) {
//		Subject subject = SecurityUtils.getSubject();
//		LoginInfo li = (LoginInfo) subject.getPrincipal();
//		boolean userAdmin = subject.isPermitted("operation:userAdmin");
//		if (user == null) {
//			if (poolId != null) {
//				if (!userAdmin) {
//					// if (li.getPoolId().longValue() == poolId.longValue()) {
//					// }
//				}
//			} else {
//				// throw new RunTimeException();
//			}
//			user = new User();
//		} else {
//			// poolId = user.getPoolId();
//			// if ((!userAdmin)
//			// && (li.getPoolId().longValue() != poolId.longValue())) {
//			// throw new IllegalUrlException();
//			// }
//			user.loadRolesForPage();
//		}
//		List<Role> roles = this.roleService.findByRemoved(
//				false, new Sort(new String[] { "name" }));
//
//		Map<String, String> roleOption = new HashMap(roles.size());
//		for (Role r : roles) {
//			if ((userAdmin) || (!r.isRemoved())) {
//				roleOption.put(r.getIdStr(), r.getName());
//			}
//		}
//		model.addAttribute("roleOption", roleOption);
//		model.addAttribute("entity", user);
//		model.addAttribute("factor", this.userService.passwordPublicFactor(subject.getSession().getId()));
//		return "user/form";
//	}
//
//	private static final Set<String> SORT_PROPERTIES = new HashSet(
//			Arrays.asList(new String[] { "id", "account", "name", "email",
//					"cellphone", "lastModified" }));
//
//	private void checkSort(Pageable pageable) {
//		for (Iterator<Sort.Order> it = pageable.getSort().iterator(); it
//				.hasNext();) {
//			String prop = ((Sort.Order) it.next()).getProperty();
//			if (!SORT_PROPERTIES.contains(prop)) {
//				throw new RuntimeException();
//			}
//		}
//	}
//
//	@RequestMapping(method = { RequestMethod.GET })
//	public String index(
//			@PageableDefault(page = 0, size = 20, sort = { "name" }, direction = Sort.Direction.DESC) Pageable pageable,
//			@RequestParam(value = "name", required = false) String name,
//			@RequestParam(value = "account", required = false) String account,
//			@RequestParam(value = "removed", required = false) Boolean removed,
//			Model model) {
//		checkSort(pageable);
//		Subject subject = SecurityUtils.getSubject();
//		LoginInfo li = (LoginInfo) subject.getPrincipal();
//		if (!subject.isPermitted("operation:userAdmin")) {
//			// poolId = li.getPoolId();
//			removed = Boolean.valueOf(false);
//			model.addAttribute("showRemoved", Boolean.valueOf(false));
//		} else {
//			model.addAttribute("showRemoved", Boolean.valueOf(true));
//			// if (poolId == null) {
//			// poolId = li.getPoolId();
//			// } else if (poolId.longValue() == 0L) {
//			// poolId = null;
//			// }
//		}
//		Page<User> page = this.userService.search(account, name, removed,
//				pageable);
//
//		model.addAttribute("page", page);
//		return "user/list";
//	}
//
//	@RequestMapping(method = { RequestMethod.POST })
//	@ResponseBody
//	public Object create(@Validated @ModelAttribute("entity") User user,
//			BindingResult result, Model model) {
//		if (result.hasErrors()) {
//			return "";
//		}
//		if (this.userService.findByAccount(user.getAccount()) != null) {
//			return "";
//		}
//		if ((StringUtils.isNotBlank(user.getEmail()))
//				&& (this.userService.findByEmail(user.getEmail()) != null)) {
//			return "";
//		}
//		if ((StringUtils.isNotBlank(user.getCellphone()))
//				&& (this.userService.findByCellphone(user.getCellphone()) != null)) {
//			return "";
//		}
//		Subject subject = SecurityUtils.getSubject();
//		LoginInfo li = (LoginInfo) subject.getPrincipal();
//		// if (!subject.isPermitted("operation:userAdmin")) {
//		// user.setPool((Pool) this.poolService.findOne(li.getPoolId()));
//		// } else {
//		// user.setPool((Pool) this.poolService.findOne(user.getPoolId()));
//		// }
//		Serializable sessionId = SecurityUtils.getSubject().getSession()
//				.getId();
//		String[] arr = this.userService.decryptPasswordString(sessionId,
//				new String[] { user.getEncryptPassword() });
//		if ((arr == null) || (arr[0] == null)) {
//			result.addError(new FieldError("entity", "entity.encryptPassword",
//					""));
//
//			return "";
//		}
//		user.setPlainPassword(arr[0]);
//		this.userService.entryptPassword(user);
//
//		this.userService.createUser(user);
//		return "/manage/user/";
//	}
//
//	@RequestMapping(value = { "/{id}" }, method = { RequestMethod.PUT })
//	@ResponseBody
//	public Object update(@PathVariable("id") Long id,
//			@Validated @ModelAttribute("entity") User user,
//			BindingResult result, Model model) {
//		if (result.hasErrors()) {
//			// DwzResponse dr = new DwzResponse();
//			// dr.setStatusCode(300);
//			// dr.setMessage(result);
//			// dr.getData().putAll(
//			// this.userService.passwordPublicFactor(SecurityUtils
//			// .getSubject().getSession().getId()));
//			return "";
//		}
//		LoginInfo li = (LoginInfo) SecurityUtils.getSubject().getPrincipal();
//		// if ((!SecurityUtils.getSubject().isPermitted("operation:userAdmin"))
//		// && (user.getPoolId().longValue() != li.getPoolId().longValue())) {
//		// throw new RuntimeException();
//		// }
//		if (StringUtils.isNotBlank(user.getEmail())) {
//			User u = this.userService.findByEmail(user.getEmail());
//			if ((u != null) && (u.getId().longValue() != id.longValue())) {
//				return "";
//			}
//		}
//		if (StringUtils.isNotBlank(user.getCellphone())) {
//			User u = this.userService.findByCellphone(user.getCellphone());
//			if ((u != null) && (u.getId().longValue() != id.longValue())) {
//				return "";
//			}
//		}
//		Serializable sessionId = SecurityUtils.getSubject().getSession()
//				.getId();
//		if (!StringUtils.isBlank(user.getEncryptPassword())) {
//			String[] arr = this.userService.decryptPasswordString(sessionId,
//					new String[] { user.getEncryptPassword() });
//			if ((arr == null) || (arr[0] == null)) {
//				result.addError(new FieldError("entity",
//						"entity.encryptPassword", ""));
//
//			}
//			user.setPlainPassword(arr[0]);
//			this.userService.entryptPassword(user);
//		}
//		this.userService.updateUser(user);
//		return "/manage/user/";
//	}
//
//	@RequestMapping(method = { RequestMethod.DELETE })
//	@ResponseBody
//	public Object remove(@RequestParam("ids") Long[] ids) {
//		if ((ids != null) && (ids.length > 0)) {
//			Subject subject = SecurityUtils.getSubject();
//
//			LoginInfo li = (LoginInfo) SecurityUtils.getSubject()
//					.getPrincipal();
//			Collection<User> c = new ArrayList(ids.length);
//			for (Long id : ids) {
//				c.add(this.userService.getForUpdate(id.longValue()));
//			}
//			if (!subject.isPermitted("operation:userAdmin")) {
//			}
//			this.userService.deleteWithAudit(c);
//		}
//		return "";
//	}
//
//	public static RequestMethod getMethod(HttpServletRequest req) {
//		return RequestMethod.valueOf(req.getMethod().toUpperCase());
//	}
//}
