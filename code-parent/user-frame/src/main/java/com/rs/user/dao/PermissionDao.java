package com.rs.user.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.entity.Permission;

public interface PermissionDao extends PagingAndSortingRepository<Permission, Long>,
	JpaSpecificationExecutor<Permission> {
//	public Iterable<Permission> findAll();
	
	public Permission findByName(String name);
}
