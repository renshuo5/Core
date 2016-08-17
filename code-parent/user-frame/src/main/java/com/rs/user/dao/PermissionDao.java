package com.rs.user.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.rs.user.entity.Permission;

public interface PermissionDao extends PagingAndSortingRepository<Permission, Long>,
	JpaSpecificationExecutor<Permission> {
//	public Iterable<Permission> findAll();
	
	public Permission findByName(String name);
}
