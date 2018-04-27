package com.encrm.system.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.encrm.publics.constants.JumpViewConstants;
import com.encrm.publics.constants.ReturnConstants;
import com.encrm.publics.constants.StateConstants;
import com.encrm.publics.util.BaseController;
import com.encrm.publics.util.UserContext;
import com.encrm.system.entity.User;
import com.encrm.system.service.IUserService;

@Controller
public class UserController extends BaseController{
	
	@Autowired
	private IUserService userService;
	
	/**
	 * 跳转用户管理界面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/system/userMang.do",method=RequestMethod.GET)
	public String userManager(Model model){
		if(UserContext.getLoginUser() != null){
			return JumpViewConstants.SYSTEM_USER_MANAGE;
		}
		return JumpViewConstants.SYSTEM_LOGIN;
	}
	
	/**
	 * 用户列表页面
	 * @param request
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="/system/userlist.do",method=RequestMethod.GET)
	public @ResponseBody String queryUserList(HttpServletRequest request, Integer currentPage, Integer pageSize){
		List<User> list = userService.queryAllUser(processPageBean(pageSize, currentPage));
		return jsonToPage(list);
	}
	
	/**
	 * 添加/修改用户
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/system/saveOrUpdate.do", method=RequestMethod.POST)
	public @ResponseBody String saveOrUpdateUser(HttpServletRequest request, User user){
		if(user != null){
			userService.saveOrUpdateUser(user);
			return ReturnConstants.SUCCESS;
		}
		return ReturnConstants.PARAM_NULL;
	}
	
	/**
	 * 根据ids，批量删除用户
	 * @param request
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/system/deleteUser.do", method=RequestMethod.POST)
	public @ResponseBody String deleteUsers(HttpServletRequest request, String ids){
		if(ids != null){
			userService.deleteByUserIds(ids);
			return ReturnConstants.SUCCESS;
		}
		return ReturnConstants.PARAM_NULL;
	}
	
	@RequestMapping(value="/system/editPassword.do", method=RequestMethod.POST)
	public @ResponseBody String editPassword(HttpServletRequest request, String newPassword, String oldPassword, String userid){
		if(StringUtils.isNotBlank(newPassword) && StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(userid)){
			//验证密码是否正确
			boolean exisPassword = userService.isExisPassword(userid, oldPassword);
			if(exisPassword){
				User user = new User();
				user.setUserid(Long.valueOf(userid));
				user.setPassword(newPassword);
				userService.saveOrUpdateUser(user);
				return ReturnConstants.SUCCESS;
			}else{
				return ReturnConstants.OLD_PASSWORD_NOT_SAME;
			}
		}
		return ReturnConstants.PARAM_NULL;
	}
}
