package com.rs.user.dao;

import java.util.List;

import org.springframework.data.jpa.mapping.JpaPersistentEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.rs.user.entity.Permission;

public interface PermissionDao extends PagingAndSortingRepository<Permission, Long>,
		JpaPersistentEntity<Permission> {
//	public Iterable<Permission> findAll(List<String> list);
}
