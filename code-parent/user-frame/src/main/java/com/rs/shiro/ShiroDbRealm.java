package com.rs.shiro;

import java.util.Set;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.model.entity.User;
import com.rs.user.model.LoginInfo;
import com.rs.user.service.UserService;

public class ShiroDbRealm extends AuthorizingRealm {

	@Autowired
	private UserService userService;

	public ShiroDbRealm() {
		super();
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		User loginInfo = (User) principals.getPrimaryPrincipal();
		Set<String> permissions = this.userService.getAllPermission(loginInfo
				.getId());
		info.addStringPermissions(permissions);
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authenticationToken) throws AuthenticationException {
//		UsernamePasswordCaptchaToken 验证码
		UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
		String username = token.getUsername();
		try {
			if (StringUtils.isBlank(username)) {
				throw new AccountException("用户名不能为空");
			}
			if ((token.getPassword() == null)
					|| (token.getPassword().length == 0)) {
				throw new CredentialsException("密码不能为空");
			}
			if (token.getPassword().length > 344) {
				throw new CredentialsException(
						"处理密码过程出现错误，请联系管理员");
			}
//			Serializable sessionId = SecurityUtils.getSubject().getSession().getId(); 
//			监测ip地址访问
			
//			if ((this.userService.captchaValidateByIp(token.getHost()) > 0L)
//					|| (this.userService.captchaValidateByAccount(username) > 0L)) {
//				if ((token.getCaptcha() == null)
//						|| (token.getCaptcha().length() == 0)) {
//					this.userService.clearCaptchaLoginVerification(sessionId);
//					LoginErrorHolder.setLoginErrorCode(10);
//					throw new IncorrectCaptchaException("ip");
//				}
//				int maxLimit = ((ReloadableConfig) ReloadableConfig.HOLDER
//						.getConfig()).getLoginFailureMaxLimit();
//				int loginFailureCheckHours = ((ReloadableConfig) ReloadableConfig.HOLDER
//						.getConfig()).getLoginFailureCheckHours();
//				if (this.userService.captchaValidateByIp(token.getHost()) > maxLimit) {
//					LoginErrorHolder.setLoginErrorCode(12);
//					throw new CredentialsException("ip"
//							+ loginFailureCheckHours
//							+ "ip" + maxLimit
//							+ "ip" + loginFailureCheckHours
//							+ "ip");
//				}
//				if (this.userService.captchaValidateByAccount(username) > maxLimit) {
//					LoginErrorHolder.setLoginErrorCode(12);
//					throw new CredentialsException("a"
//							+ loginFailureCheckHours
//							+ "aa" + maxLimit
//							+ "aa"
//							+ loginFailureCheckHours + "aa");
//				}
//				if (!this.userService.validateCaptchaLoginVerification(
//						sessionId, token.getCaptcha())) {
//					this.userService.clearCaptchaLoginVerification(sessionId);
//					LoginErrorHolder.setLoginErrorCode(10);
//					throw new IncorrectCaptchaException("aaa");
//				}
//				this.userService.clearCaptchaLoginVerification(sessionId);
//			}
			User user = this.userService.findByAccount(username);
			return doGetAuthenticationInfo(token, user);
		} catch (AuthenticationException e) {
			throw e;
		} catch (Exception e) {
			throw new AuthenticationException(
					"aa", e);
		}
	}

	private AuthenticationInfo doGetAuthenticationInfo(
			UsernamePasswordToken token, User user) {
		
//		String username = token.getUsername();
//	    ClientInfo clientInfo = SetClientInfoFilter.getClientInfo(token.getRequest(), token.getResponse(), false);
	    if (user == null)
	    {
	      String text = "用户名不存在";
//	      this.userService.recordLoginLog(clientInfo, username, null, text);
	      throw new UnknownAccountException(text);
	    }
	    if (!user.getPassword().equals(new String(token.getPassword()))){
//	      this.userService.recordLoginLog(clientInfo, username, Boolean.valueOf(false));
	      throw new IncorrectCredentialsException("用户名或密码错误");
	    }
	    if (!user.isActive())
	    {
	      String text = "用户已被锁定";
//	      this.userService.recordLoginLog(clientInfo, username, null, text);
	      throw new LockedAccountException(text);
	    }
	    if (user.isRemoved())
	    {
	      String text = "用户已被屏蔽";
//	      this.userService.recordLoginLog(clientInfo, username, null, text);
	      throw new DisabledAccountException(text);
	    }
	    if (!user.isAudited())
	    {
	      String text = "用户尚未通过审核";
//	      this.userService.recordLoginLog(clientInfo, username, Boolean.valueOf(true), text);
	      throw new AccountException(text);
	    }
//	    this.userService.recordLoginLog(clientInfo, username, Boolean.valueOf(true));
	    try
	    {
	      byte[] salt = Hex.decodeHex(user.getSalt().toCharArray());
//	      int loginConcurrentLimit = ((ReloadableConfig)ReloadableConfig.HOLDER.getConfig()).getLoginConcurrentLimit().intValue();
//	      List<SessionRecord> srs = this.userService.getLoginRecord(user.getId().longValue());
//	      if (srs.size() >= loginConcurrentLimit)
//	      {
//	        LoginErrorHolder.setLoginErrorCode(12);
//	        
//	        throw new LoginConcurrentLimitException("用户同时登录了" + srs.size() + "次，已到达最大限制");
//	      }
	      return new SimpleAuthenticationInfo(new LoginInfo(user.getId(),(String)SecurityUtils.getSubject().getSession().getId(),user.getAccount()), 
	    		  user.getPassword(), ByteSource.Util.bytes(salt), getName());
	    }
	    catch (DecoderException e)
	    {
	      throw new RuntimeException(e);
	    }
	}
}
