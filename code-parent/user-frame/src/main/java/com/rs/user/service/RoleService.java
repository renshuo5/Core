package com.rs.user.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.model.entity.Permission;
import com.model.entity.Role;
import com.rensframework.core.service.BaseService;
import com.rs.user.dao.PermissionDao;
import com.rs.user.dao.RoleDao;

@Service
public class RoleService extends BaseService<Role> {

	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private PermissionDao permissionDao;

	@Override
	protected PagingAndSortingRepository<Role, Long> getDao() {
		return roleDao;
	}

	public Role findByName(String name) {
		return this.roleDao.findByName(name);
	}

	public Page<Role> findByRemoved(boolean removed, Pageable pageable) {
		return this.roleDao.findByRemoved(removed, pageable);
	}

	public Page<Role> findByNameContaining(String name, Pageable pageable) {
		return this.roleDao.findByNameContaining(name, pageable);
	}

	public Page<Role> findByRemovedAndNameContaining(boolean removed,
			String name, Pageable pageable) {
		return this.roleDao.findByRemovedAndNameContaining(removed, name,
				pageable);
	}

	@Transactional(readOnly = false)
	public void createRole(Role r) {
		Iterator<Permission> it;
		if ((r.getPermissionsForPage() != null)
				&& (r.getPermissionsForPage().size() > 0)) {
			r.setPermissions(new HashSet(r.getPermissionsForPage().size()));
//			Iterable<Permission> pi = this.permissionDao.findAll(r.getPermissionsForPage());
//			
//			for (it = pi.iterator(); it.hasNext();) {
//				r.getPermissions().add(it.next());
//			}
		}
		saveWithAudit(r);
	}

	@Transactional(readOnly = false)
	public void updateRole(Role r) {
		List<String> nl = r.getPermissionsForPage();
		if (nl == null) {
			nl = new ArrayList(0);
		}
		r.loadPermissionsForPage();
		List<String> ol = r.getPermissionsForPage();

		Map<String, Permission> permMap = new HashMap(r.getPermissions().size());
		for (Permission p : r.getPermissions()) {
			permMap.put(p.getPerm(), p);
		}
		for (String s : ol) {
			if (!nl.contains(s)) {
				r.getPermissions().remove(permMap.get(s));
			}
		}
		for (String s : nl) {
			if (!ol.contains(s)) {
				r.getPermissions().add(this.permissionDao.findOne(new Long(s)));
			}
		}
		saveWithAudit(r);
	}

	
	public List<Role> findByRemoved(boolean removed, Sort sort) {
		return this.roleDao.findByRemoved(removed, sort);
	}

}
