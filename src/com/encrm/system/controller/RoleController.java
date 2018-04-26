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
import com.encrm.publics.util.BaseController;
import com.encrm.publics.util.UserContext;
import com.encrm.system.entity.Menu;
import com.encrm.system.entity.Role;
import com.encrm.system.entity.Role_menu;
import com.encrm.system.service.IMenuService;
import com.encrm.system.service.IRoleService;
import com.encrm.system.service.impl.RoleServiceImpl;

@Controller
public class RoleController extends BaseController {

	@Autowired
	private IRoleService roleService;

	@Autowired
	private IMenuService menuService;
	
	/**
	 * 跳转角色管理页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/system/roleMang.do", method = RequestMethod.GET)
	public String roleManager(Model model) {
		if (UserContext.getLoginUser() != null) {
			return JumpViewConstants.SYSTEM_ROLE_MANAGE;
		}
		return JumpViewConstants.SYSTEM_LOGIN;
	}

	/**
	 * 查询角色信息列表
	 * 
	 * @param request
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            每页显示条数
	 * @return
	 */
	@RequestMapping(value = "/role/queryAllRole.do", method = RequestMethod.GET)
	public @ResponseBody
	String queryAllRole(HttpServletRequest request, Integer currentPage,
			Integer pageSize) {
		List<Role> list = roleService.queryAllRole(processPageBean(pageSize,
				currentPage));
		return jsonToPage(list);
	}

	/**
	 * 添加/修改角色信息
	 * 
	 * @param request
	 * @param role
	 * @return
	 */
	@RequestMapping(value = "/role/addOrUpdateRole.do", method = RequestMethod.POST)
	public @ResponseBody
	String addOrUpdateRole(HttpServletRequest request, Role role) {
		if (role != null) {
			roleService.addOrUpdateRole(role);
			return ReturnConstants.SUCCESS;
		}
		return ReturnConstants.PARAM_NULL;
	}

	/**
	 * 批量删除角色信息
	 * 
	 * @param request
	 * @param ids
	 *            要删除的角色信息 id
	 * @return
	 */
	@RequestMapping(value = "/role/deleteRole.do", method = RequestMethod.POST)
	public @ResponseBody
	String addOrUpdateRole(HttpServletRequest request, String ids) {
		if (StringUtils.isNotBlank(ids)) {
			roleService.deleteByRoleIds(ids);
			return ReturnConstants.SUCCESS;
		}
		return ReturnConstants.PARAM_NULL;
	}

	/**
	 * 权限分配，查询所有菜单信息，并且已经分配过的菜单，复选框自动勾选
	 * 
	 * @param request
	 * @param roleid
	 * @return
	 */
	@RequestMapping(value = "/rolemenu/queryAllMenuAndSelected.do", method = RequestMethod.GET)
	public @ResponseBody
	String queryAllMenuAndSelected(HttpServletRequest request, String roleid) {
		if (StringUtils.isNotBlank(roleid)) {
			// 查询所有菜单信息
			List<Menu> menus = menuService.queryAllMenu();// ---分级展示
			// 查询已经分配过的菜单信息
			List<Menu> menusSelected = menuService
					.queryAlreadySelectedMenu(roleid);// ---不分级
			if (menusSelected != null && menusSelected.size() > 0) {
				// 给一级菜单打上勾
				for (int i = 0; i < menus.size(); i++) {
					Long menuid = menus.get(i).getMenuid(); // 上一级的菜单id
					for (int j = 0; j < menusSelected.size(); j++) {
						Long menuSelectedid = menusSelected.get(j).getMenuid(); // 分配过的菜单id
						if (menuid == menuSelectedid) { // 相等，当前菜单id为选中
							menus.get(i).setSelected(true);
							break;
						}
					}
				}

				// 给一级菜单的子菜单打勾
				for (int i = 0; i < menus.size(); i++) {
					List<Menu> menusChildren = menus.get(i).getChildren();// 一级菜单的子菜单
					for(int j=0; j<menusChildren.size(); j++){
						Long menuChildrenid = menusChildren.get(j).getMenuid(); //子菜单的主键id
						for(int k=0; k<menusSelected.size(); k++){
							Long menusSelectedid = menusSelected.get(k).getMenuid();//分配过的菜单id
							if(menuChildrenid == menusSelectedid){//相等，当前子菜单id为选中
								menusChildren.get(j).setSelected(true);
								break;
							}
						}
					}
				}

			}
			return jsonToPage(menus);// 把打勾后的菜单列表返回
		}
		return ReturnConstants.PARAM_NULL;
	}
	
	/**
	 * 分配权限，保存菜单信息，
	 * 1.删除数据开始角色对应的菜单信息
	 * 2.保存一份最新的角色对应菜单信息数据即可
	 * @param request
	 * @param roleid
	 * @param menuid
	 * @return
	 */
	@RequestMapping(value = "/rolemenu/assignMenu.do", method = RequestMethod.POST)
	public @ResponseBody String assignMenu(HttpServletRequest request, String roleid, String menuid) {
		if (StringUtils.isNotBlank(roleid)) {
			// 删除数据开始角色对应的菜单信息
			menuService.deleteRoleMenu(roleid);
			// 保存一份最新的角色对应菜单信息数据即可
			Role_menu role_menu = new Role_menu();
			role_menu.setRoleid(Long.valueOf(roleid));
			for (String mid : menuid.split(",")) {
				role_menu.setMenuid(Long.valueOf(mid));
				menuService.saveRoleMenu(role_menu);
				role_menu.setMenuid(null);
			}
			return ReturnConstants.SUCCESS;
		}
		return ReturnConstants.PARAM_NULL;
	}
}
