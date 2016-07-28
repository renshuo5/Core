package com.rs.user.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.rs.user.entity.User;

public interface UserDao extends PagingAndSortingRepository<User, Long>,
		JpaSpecificationExecutor<User> {

	public abstract User findByAccount(String paramString);

	public abstract List<User> findByNameContainingAndRemoved( String paramString, boolean paramBoolean);

	public abstract User findByEmail(String paramString);

	public abstract User findByCellphone(String paramString);

	public abstract List<User> findByNameContaining(String paramString);

}
