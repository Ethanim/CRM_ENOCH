package com.encrm.publics.interceptorfiles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.encrm.publics.util.UserContext;
import com.encrm.system.entity.User;

/**
 * 后台登陆拦截器 拦截.do结尾的请求
 */
public class BackgroundLoginInterceptor implements HandlerInterceptor {

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object arg2) throws Exception {
		String servletPath = request.getServletPath();
		User loginUser = UserContext.getLoginUser();
		// 如果登录成功
		if (loginUser != null) {
			return true;
		} else {
			if ("/login.do".equals(servletPath)) {
				return true;
			} else {
				response.sendRedirect(request.getContextPath() + "/login.do");
				return false;
			}
		}
	}

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object arg2, Exception arg3)
			throws Exception {

	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object arg2, ModelAndView arg3)
			throws Exception {

	}
}
