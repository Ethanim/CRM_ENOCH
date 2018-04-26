package com.encrm.system.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.encrm.publics.constants.JumpViewConstants;
import com.encrm.publics.constants.ReturnConstants;
import com.encrm.publics.util.BaseController;
import com.encrm.publics.util.UserContext;
import com.encrm.system.entity.Menu;
import com.encrm.system.service.IMenuService;

@Controller
public class MenuController extends BaseController{
	
	@Autowired
	private IMenuService menuService;
	
	/**
	 * 跳转菜单列表页
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/system/menuMang.do",method=RequestMethod.GET)
	public String menuManager(Model model){
		if(UserContext.getLoginUser() != null){
			return JumpViewConstants.SYSTEM_MENU_MANAGE;
		}
		return JumpViewConstants.SYSTEM_LOGIN;
	}
	
	/**
	 * 查询所有菜单信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/menu/queryAllMenu.do",method=RequestMethod.GET)
	public @ResponseBody String queryAllMenu(HttpServletRequest request){
		List<Menu> menus = menuService.queryAllMenu();
		return jsonToPage(menus);
	}
	
	/**
	 * 添加或修改菜单
	 * @param request
	 * @param menu
	 * @return
	 */
	@RequestMapping(value="/menu/addOrUpdateMenu.do", method=RequestMethod.POST)
	public @ResponseBody String addOrUpdateMenu(HttpServletRequest request, Menu menu){
		if(menu != null){
			menuService.addOrUpdateMenu(menu);
			return ReturnConstants.SUCCESS;
		}
		return ReturnConstants.PARAM_NULL;
	}
	
	/**
	 * 菜单删除功能
	 * 
	 * 1.如果菜单存在下一级菜单，则不允许删除
	 * 2.如果菜单已经被分配，则不允许删除
	 *  	error1 = [菜单：用户管理，菜单管理]  存在下一级菜单信息，不允许删除
	 *  	error2 = [菜单：角色管理，部门管理]  存在权限分配，不允许删除
	 *  
	 * @param request
	 * @param ids 菜单主键 id，多个用逗号隔开
	 * @return
	 */
	@RequestMapping(value="/menu/delete.do", method=RequestMethod.POST)
	public @ResponseBody String deleteMenus(HttpServletRequest request, String ids){
		if(StringUtils.isNotBlank(ids)){
			boolean isHasChildren = false;
			boolean isHasRoleMenu = false;
			
			StringBuffer error1 = new StringBuffer();
			StringBuffer error2 = new StringBuffer();
			
			for(String id : ids.split(",")){
				//查询菜单id，是否存在下一级菜单
				if(menuService.isHasChildrenMenu(id)){ //true 代表有下一级菜单
					if(!isHasChildren){
						isHasChildren = true;
					}
					if(error1.length() == 0){
						error1.append("[菜单：");
					}
					Menu menu = menuService.queryMenuById(id);
					error1.append(menu != null ? menu.getMenuname() : "").append(",");
					continue;
				}
				//查询菜单id，是否分配权限
				if(menuService.isHasRoleMenu(id)){ //true 代表存在权限分配
					if(!isHasRoleMenu){
						isHasRoleMenu = true;
					}
					if(error2.length() == 0){
						error2.append("[菜单：");
					}
					Menu menu = menuService.queryMenuById(id);
					error2.append(menu != null ? menu.getMenuname() : "").append(",");
					continue;
				}
				//如果当前菜单id，即不存在下级菜单，也没被分配权限，直接删除
				menuService.deleteMenuById(id);
			}
			
			//如果满足则都不允许删除，返回错误信息
			if(isHasChildren){
				error1.deleteCharAt(error1.length() - 1).append("]  存在下一级菜单信息，不允许删除");
			}
			if(isHasRoleMenu){
				error2.deleteCharAt(error2.length() - 1).append("]  存在权限分配，不允许删除");
			}
			
			String msg = error1.toString() + error2.toString();
			if(StringUtils.isNotBlank(msg)){
				return msg;
			}
			//如果都不满足，删除成功
			return ReturnConstants.SUCCESS;
		}
		return ReturnConstants.PARAM_NULL;
	}
}
