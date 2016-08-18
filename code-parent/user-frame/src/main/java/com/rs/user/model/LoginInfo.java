package com.rs.user.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.rensframework.core.model.BaseModel;

@Data
@EqualsAndHashCode(callSuper = false)
public class LoginInfo extends BaseModel {

	private static final long serialVersionUID = -2796047668846997499L;

	private Long userId;

	private String account;
	
	protected String loginId;
	
	public LoginInfo(Long userId, String loginId, String account) {
		this.userId = userId;
		this.loginId = loginId;
		this.account = account;
	}

}
