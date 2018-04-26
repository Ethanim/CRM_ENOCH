package com.encrm.publics.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.encrm.system.entity.User;

/**
 * 用户登录状态封装
 */
public class UserContext {
	
	public static final String LOGIN_USER = "login_user"; 
	
	private static HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}
	
	public static User getLoginUser(){
		return (User) getRequest().getSession(true).getAttribute(UserContext.LOGIN_USER);
	}
	
	public static void setLoginUser(User backuser){
		getRequest().getSession(true).setAttribute(UserContext.LOGIN_USER, backuser);
	}
	
	public static void clearLoginUser(){
		getRequest().getSession(true).removeAttribute(UserContext.LOGIN_USER);
	}
}
