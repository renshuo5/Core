package com.rs.user.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.mapping.JpaPersistentEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.rs.user.entity.Role;

public interface RoleDao extends PagingAndSortingRepository<Role, Long>,
		JpaPersistentEntity<Role> {

	public abstract Page<Role> findByRemoved(boolean paramBoolean,
			Pageable paramPageable);

	public abstract Page<Role> findByRemovedAndNameContaining(
			boolean paramBoolean, String paramString, Pageable paramPageable);

	public abstract Role findByName(String paramString);

	public abstract List<Role> findByRemoved(
			boolean paramBoolean, Sort paramSort);

	public abstract Page<Role> findByNameContaining(String paramString,
			Pageable paramPageable);

}
