package com.rs.user.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.entity.User;

public interface UserDao extends PagingAndSortingRepository<User, Long>,
		JpaSpecificationExecutor<User> {

	public User findByAccount(String paramString);

	public User findByEmail(String paramString);

	public User findByCellphone(String paramString);

	public List<User> findByNameContaining(String paramString);

}
