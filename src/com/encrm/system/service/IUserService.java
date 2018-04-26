package com.encrm.system.service;

import java.util.List;

import com.encrm.publics.util.PageBean;
import com.encrm.system.entity.Menu;
import com.encrm.system.entity.User;

public interface IUserService {
	/**
	 * 根据邮箱查询用户信息
	 * @param email
	 * @return
	 */
	public User queryUserByEmail(String email);
	
	/**
	 * 验证密码是否匹配
	 * @param userid 用户主键 ID
	 * @param password 密码
	 * @return
	 */
	public boolean isExisPassword(String userid, String password);
	
	/**
	 * 根据角色主键 ID,查询当前角色对应的菜单信息
	 * @param roleId
	 * @return
	 */
	public List<Menu> getMenusByRoleId(String roleId);
	
	/**
	 * 查询用户信息列表
	 * @param pageBean
	 * @return
	 */
	public List<User> queryAllUser(PageBean pageBean);
	
	/**
	 * 增加/修改用户信息
	 * @param user
	 */
	public void saveOrUpdateUser(User user);
	
	/**
	 * 删除用户信息，批量删除
	 * @param ids 用户主键 id，多个用 逗号号隔开
	 */
	public void deleteByUserIds(String ids);
}
