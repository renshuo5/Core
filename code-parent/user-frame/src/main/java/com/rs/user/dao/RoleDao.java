package com.rs.user.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.entity.Role;

public interface RoleDao extends PagingAndSortingRepository<Role, Long>,
	JpaSpecificationExecutor<Role> {

	public Page<Role> findByRemoved(boolean paramBoolean,
			Pageable paramPageable);

	public Page<Role> findByRemovedAndNameContaining(
			boolean paramBoolean, String paramString, Pageable paramPageable);

	public Role findByName(String paramString);

	public List<Role> findByRemoved(
			boolean paramBoolean, Sort paramSort);

	public Page<Role> findByNameContaining(String paramString,
			Pageable paramPageable);

}
