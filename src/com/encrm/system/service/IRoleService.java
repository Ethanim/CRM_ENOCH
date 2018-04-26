package com.encrm.system.service;

import java.util.List;

import com.encrm.publics.util.PageBean;
import com.encrm.system.entity.Role;

public interface IRoleService {
	/**
	 * 查询所有角色信息
	 * @param pageBean
	 * @return
	 */
	public List<Role> queryAllRole(PageBean pageBean);
	
	/**
	 * 添加/修改角色
	 * @param role
	 */
	public void addOrUpdateRole(Role role);
	
	/**
	 * 通过角色 ID，批量删除角色
	 * @param ids
	 */
	public void deleteByRoleIds(String ids);
	
}
