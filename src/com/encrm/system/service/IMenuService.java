package com.encrm.system.service;

import java.util.List;

import com.encrm.system.entity.Menu;
import com.encrm.system.entity.Role_menu;

public interface IMenuService {
	
	/**
	 * 查询所有菜单
	 * @return
	 */
	public List<Menu> queryAllMenu();
	
	/**
	 * 查询当前角色已经分配过的菜单信息
	 * @param roleid
	 * @return
	 */
	public List<Menu> queryAlreadySelectedMenu(String roleid);
	
	/**
	 * 根据角色id，删除数据库中对应的菜单信息数据
	 * @param roleid
	 */
	public void deleteRoleMenu(String roleid);
	
	/**
	 * 保存角色-菜单关系数据
	 * @param menu
	 */
	public void saveRoleMenu(Role_menu menu);
	
	/**
	 * 添加/修改菜单
	 * @param menu
	 */
	public void addOrUpdateMenu(Menu menu);
	
	/**
	 * 根据菜单主键 id，判断当前菜单是否有下一级菜单
	 * @param id
	 * @return
	 */
	public boolean isHasChildrenMenu(String id);
	
	/**
	 * 根据菜单主键 id，判断当前菜单是否分配权限
	 * @param id
	 * @return
	 */
	public boolean isHasRoleMenu(String id);
	
	/**
	 * 根据菜单 id，查询菜单信息
	 * @param id
	 * @return
	 */
	public Menu queryMenuById(String id);
	
	/**
	 * 根据菜单主键 id，删除菜单
	 * @param id
	 */
	public void deleteMenuById(String ids);
}
