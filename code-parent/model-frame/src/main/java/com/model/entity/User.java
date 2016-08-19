package com.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.hibernate.validator.constraints.Email;

import com.rensframework.core.entity.Auditable;
import com.rensframework.core.entity.TimeEntity;

@Entity
@Table(name = "usr_user")
@Data
@Cacheable
@EqualsAndHashCode(callSuper = false)
public class User extends TimeEntity implements Auditable<User> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2859867506337037220L;

	@NotNull
	@Size(min = 2, max = 64)
	@Column(length = 64, unique = true)
	private String account;

	@Column(length = 200, nullable = false)
	private String password;
	
	@Transient
	private String plainPassword;

	@Transient
	@NotNull
	private String encryptPassword;

	@Column(length = 20, nullable = false)
	private String salt;

	@NotNull
	@Size(min = 2, max = 64)
	@Column(length = 64)
	private String name;

	@Size(min = 7, max = 32)
	@Column(nullable = true, length = 32, unique = true)
	private String cellphone;

	private boolean cellphoneVerified = false;
	
	@Size(min = 7, max = 32)
	@Column(nullable = true, length = 32)
	private String telephone;
	
	@Size(min = 3, max = 255)
	@Email
	@Column(length = 255, unique = true)
	private String email;

	private boolean emailVerified = false;
	
	private boolean active = true;

	private boolean audited;
	
	private int level;
	
	private boolean removed;

	@ManyToMany(cascade = { javax.persistence.CascadeType.PERSIST,
			javax.persistence.CascadeType.MERGE })
	@JoinTable(name = "USR_USER_ROLE", inverseJoinColumns = { @JoinColumn(name = "ROLE_ID") }, joinColumns = { @JoinColumn(name = "USER_ID") })
	private Set<Role> roles;

	@Transient
	private List<String> rolesForPage;
	@Transient
	public void loadRolesForPage() {
		this.rolesForPage = new ArrayList(this.roles.size());
		for (Role r : this.roles) {
			this.rolesForPage.add(r.getIdStr());
		}
	}

	@Transient
	private User orig;

	@Override
	public String getEntityName() {
		return "用户表";
	}

	@Override
	public String getIdStr() {
		return getId().toString();
	}

	@Override
	public String getName() {
		return "user";
	}

}
