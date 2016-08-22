package com.rs.user.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.model.entity.User;
import com.rs.user.service.UserService;
import com.rs.util.EncryptUtils;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = {"/login", "/login;JSESSIONID={sessionId}", "/signon", "/signon;JSESSIONID={sessionId}"})
	public String redirectLogin() {
		Subject subject = SecurityUtils.getSubject();  
        if (subject.isAuthenticated()) {  
            return "redirect:/manage";
        } else {  
            return "user/login";
        }  
	}

	@RequestMapping(value = { "/dologin", "/dologin;JSESSIONID={sessionId}",
			"/dosignon/", "/dosignon/;JSESSIONID={sessionId}" }, method = { RequestMethod.POST })
	public String login(@RequestParam(value="username", required=false) String userName,
			@RequestParam(value="password", required=false) String password,Model model, HttpServletRequest req) {
		
		String msg = "";  
	    System.out.println(userName);  
	    System.out.println(password);
	    
	    
	    User user = userService.findByAccount(userName);
	    UsernamePasswordToken token = new UsernamePasswordToken(userName, EncryptUtils.encryptSHA1(user.getSalt(), password));
	    token.setRememberMe(true);
	    Subject subject = SecurityUtils.getSubject(); 
	    try {  
	        subject.login(token);
	        
//	        new SimpleAuthenticationInfo(new LoginInfo(user.getId(), (String)SecurityUtils.getSubject().getSession().getId(), user.getAccount(), user.getPoolId().longValue()), user.getPassword(), ByteSource.Util.bytes(salt), getName());
	        if (subject.isAuthenticated()) {  
	            return "redirect:/manage";
	        } else {  
	            return "login";
	        }  
	    } catch (IncorrectCredentialsException e) {  
	        msg = "登录密码错误. Password for account " + token.getPrincipal() + " was incorrect.";  
	        model.addAttribute("message", msg);  
	        System.out.println(msg);  
	    } catch (ExcessiveAttemptsException e) {  
	        msg = "登录失败次数过多";  
	        model.addAttribute("message", msg);  
	        System.out.println(msg);  
	    } catch (LockedAccountException e) { 
	        msg = "帐号已被锁定. The account for username " + token.getPrincipal() + " was locked.";  
	        model.addAttribute("message", msg);  
	        System.out.println(msg);  
	    } catch (DisabledAccountException e) {  
	        msg = "帐号已被禁用. The account for username " + token.getPrincipal() + " was disabled.";  
	        model.addAttribute("message", msg);  
	        System.out.println(msg);  
	    } catch (ExpiredCredentialsException e) {  
	        msg = "帐号已过期. the account for username " + token.getPrincipal() + "  was expired.";  
	        model.addAttribute("message", msg);  
	        System.out.println(msg);  
	    } catch (UnknownAccountException e) {  
	        msg = "帐号不存在. There is no user with username of " + token.getPrincipal();  
	        model.addAttribute("message", msg);  
	        System.out.println(msg);  
	    } catch (UnauthorizedException e) {  
	        msg = "您没有得到相应的授权！" + e.getMessage();  
	        model.addAttribute("message", msg);  
	        System.out.println(msg);  
	    }  
	    return "user/login";  
	}
	
	@RequestMapping(value = "/logout",method = { RequestMethod.GET })
	public String logout() {
		Subject currentUser = SecurityUtils.getSubject();
		try {
			currentUser.logout();
		} catch (AuthenticationException e) {
			e.printStackTrace();

		}
		return "user/login";
	}

}
