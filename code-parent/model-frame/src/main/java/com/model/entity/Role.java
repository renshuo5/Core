package com.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.rensframework.core.entity.Auditable;
import com.rensframework.core.entity.TimeEntity;

@Entity
@Table(name = "usr_role", uniqueConstraints = { @javax.persistence.UniqueConstraint(columnNames = { "name" }) })
@Data
@EqualsAndHashCode(callSuper = true)
public class Role extends TimeEntity implements Auditable<Role> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8346201418658854089L;

	@NotNull
	@Size(min = 2, max = 64)
	@Column(length = 64)
	private String name;

//	private int level;

	private boolean removed = false;

	@ManyToMany(cascade = { javax.persistence.CascadeType.PERSIST,
			javax.persistence.CascadeType.MERGE }, mappedBy = "roles")
	private Set<User> users;

	@ManyToMany(cascade = { javax.persistence.CascadeType.PERSIST,
			javax.persistence.CascadeType.MERGE })
	@JoinTable(name = "USR_ROLE_PERMISSION", inverseJoinColumns = { @javax.persistence.JoinColumn(name = "PERMISSION") }, joinColumns = { @javax.persistence.JoinColumn(name = "ROLE_ID") })
	private Set<Permission> permissions;

	@Size(max = 255)
	@Column(length = 255, nullable = true)
	private String remark;

	@Transient
	private List<String> permissionsForPage;

	@Transient
	public void loadPermissionsForPage() {
		this.permissionsForPage = new ArrayList(this.permissions.size());
		for (Permission p : this.permissions) {
			this.permissionsForPage.add(p.getPerm());
		}
	}

	@Override
	public String getEntityName() {
		return "角色";
	}

	@Override
	public String getIdStr() {
		return this.getId().toString();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Transient
	private Role orig;

	@Override
	public Role getOrig() {
		return orig;
	}

	@Override
	public void setOrig(Role orig) {
		this.orig = orig;
	}

}
