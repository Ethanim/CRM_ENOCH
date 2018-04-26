package com.encrm.system.service;

import java.util.List;

import com.encrm.publics.util.PageBean;
import com.encrm.system.entity.Dept;
import com.encrm.system.entity.Role;

public interface IDeptService {
	/**
	 * 查询所有部门信息
	 * @return
	 */
	public List<Dept> queryAllDepts(PageBean pageBean);

	/**
	 * 根据部门 ID, 查询当前部门下对应的角色信息
	 * @param deptId
	 * @return
	 */
	public List<Role> queryRolesByDeptId(String deptid);
	
	/**
	 * 增加/修改菜单
	 * @param dept
	 */
	public void saveOrUpdateDept(Dept dept);
	
	/**
	 * 根据 id 批量删除部门
	 * @param ids
	 */
	public void deleteDeptById(String ids);
}
