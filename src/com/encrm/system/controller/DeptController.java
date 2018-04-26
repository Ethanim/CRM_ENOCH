package com.encrm.system.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import sun.org.mozilla.javascript.internal.ast.Jump;

import com.encrm.publics.constants.JumpViewConstants;
import com.encrm.publics.constants.ReturnConstants;
import com.encrm.publics.util.BaseController;
import com.encrm.publics.util.UserContext;
import com.encrm.system.entity.Dept;
import com.encrm.system.entity.Role;
import com.encrm.system.service.IDeptService;

@Controller
public class DeptController extends BaseController{
	
	@Autowired
	private IDeptService deptService;

	/**
	 * 查询所有部门信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/dept/queryDept.do", method=RequestMethod.GET)
	public @ResponseBody String queryAllDepts(HttpServletRequest request, Integer currentPage, Integer pageSize){
		List<Dept> depts = deptService.queryAllDepts(processPageBean(pageSize, currentPage));
		return jsonToPage(depts);
	}
	
	/**
	 * 根据部门 ID，查询当前部门下的角色信息列表
	 * @param request
	 * @param deptId 部门 ID
	 * @return
	 */
	@RequestMapping(value="/role/queryRoleByDeptid.do", method=RequestMethod.GET)
	public @ResponseBody String queryRolesByDeptId(HttpServletRequest request, String deptid){
		List<Role> list = deptService.queryRolesByDeptId(deptid);
		return jsonToPage(list);
	}
	
	/**
	 * 跳转登录界面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/system/deptMang.do", method=RequestMethod.GET)
	public String deptManager(HttpServletRequest request){
		if(UserContext.getLoginUser() != null){
			return JumpViewConstants.SYSTEM_DEPT_MANAGE;
		}
		return JumpViewConstants.SYSTEM_LOGIN;
	}
	
	@RequestMapping(value="/dept/saveOrUpdate.do", method=RequestMethod.POST)
	public @ResponseBody String saveOrUpdateDept(HttpServletRequest request, Dept dept){
		if(dept != null){
			deptService.saveOrUpdateDept(dept);
			return ReturnConstants.SUCCESS;
		}
		return ReturnConstants.PARAM_NULL;
	}
	
	@RequestMapping(value="/dept/delete.do", method=RequestMethod.POST)
	public @ResponseBody String deleteDeptById(HttpServletRequest request, String ids){
		if(StringUtils.isNotBlank(ids)){
			deptService.deleteDeptById(ids);
			return ReturnConstants.SUCCESS;
		}
		return ReturnConstants.PARAM_NULL;
	}
}
