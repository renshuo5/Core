package com.model.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

import com.rensframework.core.entity.Entity;

@javax.persistence.Entity
@Table(name="usr_permission")
@Data
public class Permission implements Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6558432664487802308L;

	@Id
	@Column(length = 255, nullable = false, unique = true)
	private String perm;
	
	@Column(length = 64, nullable = false)
	private String category;
	
	@Column(length = 64, nullable = false, unique = true)
	private String name;
	
	@ManyToMany(cascade = { javax.persistence.CascadeType.PERSIST,
			javax.persistence.CascadeType.MERGE }, mappedBy = "permissions")
	private Set<Role> roles;

}
