package com.encrm.system.controller;

import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.event.UndoableEditListener;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.encrm.publics.constants.JumpViewConstants;
import com.encrm.publics.constants.ReturnConstants;
import com.encrm.publics.util.ContextUtil;
import com.encrm.publics.util.MD5Tools;
import com.encrm.publics.util.UserContext;
import com.encrm.system.entity.Menu;
import com.encrm.system.entity.User;
import com.encrm.system.service.IUserService;


@Controller
public class UserLoginController {
	
	private static final String COOKIE_KEY = "_auth_";
	private static final String COOKIE_SPI = "_#_";
	
	@Autowired
	private IUserService UserService;
	
	/**
	 * 跳转登录界面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/login.do", method=RequestMethod.GET)
	public String index(Model model){
		if(UserContext.getLoginUser() != null){
			return "redirect:/main.do";
		}
		return JumpViewConstants.SYSTEM_LOGIN;
	}
	
	/**
	 * 显示左侧菜单
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/main.do", method=RequestMethod.GET)
	public String main(Model model){
		if(UserContext.getLoginUser() != null){
			List<Menu> menus = UserService.getMenusByRoleId(UserContext.getLoginUser().getRoleid().toString());
			model.addAttribute("menus", menus);
			return JumpViewConstants.SYSTEM_INDEX;
		}
		return JumpViewConstants.SYSTEM_LOGIN;
	}
	
	/**
	 * 用户登录
	 * @param request
	 * @param response
	 * @param email
	 * @param password
	 * @param sign
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/login.do", method=RequestMethod.POST)
	public String login(HttpServletRequest request,HttpServletResponse response, String email, String password, String sign, Model model){
		
		//isNotEmpty：只会判断参数是否为 null
		//isNotBlank：会判断参数是否为 null 和 ""
		if(StringUtils.isNotBlank(email) && StringUtils.isNotBlank(password)){
			//利用 Spring 容器获取 Map 中的属性值
			email = email + ContextUtil.getInitConfig("email_suffix");
			
			//验证用户是否存在
			User user = UserService.queryUserByEmail(email);
			if(user == null){//用户不存在
				model.addAttribute("msg", ReturnConstants.USER_NOT_EXIST);
				return JumpViewConstants.SYSTEM_LOGIN;
			}
			
			//验证密码是否匹配
			boolean isExis = UserService.isExisPassword(String.valueOf(user.getUserid()), password);
			if(!isExis){
				model.addAttribute("msg", ReturnConstants.PASSWORD_ERROR);
				return JumpViewConstants.SYSTEM_LOGIN;
			}
			
			//cookie
			Cookie cok = new Cookie(COOKIE_KEY, URLEncoder.encode(user.getUsername()) + COOKIE_SPI + MD5Tools.encode(user.getEmail()));
			cok.setPath("/");
			cok.setMaxAge(-1);//-1：立即创建，并且在登录成功之后，就生效 //0：在客户端关闭浏览器之后，即失效
						
			response.addCookie(cok);
			
			//处理 session
			UserContext.setLoginUser(user);
			request.getSession(true).setAttribute("loginName", user.getUsername());
			request.getSession(true).setAttribute("ischange", user.getIschange());
			
			//成功，跳转到首页
			//return JumpViewConstants.SYSTEM_INDEX;
			return "redirect:/main.do";
		}
		return ReturnConstants.PARAM_NULL;
	}
	
	/**
	 * 用户退出
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="logout.do",method=RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response){
		// 清除 session
		UserContext.clearLoginUser();
		
		// 清除服务器中的 cookie 数据
		Cookie cok = new Cookie(COOKIE_KEY, null);
		cok.setMaxAge(0);
		cok.setPath("/");
		response.addCookie(cok);
		
		// 清除客户端中的 cookie 数据
		Cookie cokSessionID = new Cookie("JSESSIONID", null);
		cokSessionID.setPath(request.getContextPath());
		response.addCookie(cokSessionID);
		
		//跳转登录页面
		return "redirect:/main.do";
	}
	
}
