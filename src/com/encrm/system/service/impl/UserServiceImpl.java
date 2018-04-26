package com.encrm.system.service.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.encrm.publics.dao.IDataAccess;
import com.encrm.publics.util.MD5Tools;
import com.encrm.publics.util.PageBean;
import com.encrm.publics.util.UserContext;
import com.encrm.system.entity.Menu;
import com.encrm.system.entity.User;
import com.encrm.system.service.IUserService;
import com.sun.faces.taglib.jsf_core.IdTagParserImpl;

@Service
@Transactional(rollbackFor=Exception.class)
public class UserServiceImpl implements IUserService{
	
	@Autowired
	private IDataAccess<User> userDao;
	
	@Autowired
	private IDataAccess<Menu> menuDao;

	@Override
	public User queryUserByEmail(String email) {
		Map<String, Object> param = new HashMap<>();
		param.put("email", email);
		List<User> list = userDao.queryByStatment("queryUserByEmail", param, null);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public boolean isExisPassword(String userid, String password) {
		Map<String, Object> param = new HashMap<>();
		param.put("userid", userid);
		param.put("password", MD5Tools.encode(password));
		List<User> list = userDao.queryByStatment("isExisPassword", param, null);
		if(list != null && list.size() > 0){
			return true;
		}
		return false;
	}

	@Override
	public List<Menu> getMenusByRoleId(String roleId) {
		HashMap<String, Object> param = new HashMap<>();
		param.put("roleId", roleId);
		param.put("isparid", "true");
		List<Menu> list = menuDao.queryByStatment("getMenusByRoleId", param, null);
		if(list != null && list.size() > 0){
			for(int i=0; i< list.size(); i++){
				Long menuid = list.get(i).getMenuid();//获取一级菜单 ID
				param.clear();
				param.put("roleId", roleId);
				param.put("menuparaid", menuid);//将一级菜单 ID，当作二级菜单的父 ID
				param.put("isparid", null);
				List<Menu> listChildren = menuDao.queryByStatment("getMenusByRoleId", param, null);//查询对应的二级菜单
				list.get(i).setChildren(listChildren);//把二级菜单放入一级菜单里
			}
		}
		return list;
	}

	@Override
	public List<User> queryAllUser(PageBean pageBean) {
		List<User> user = userDao.queryByStatment("queryAllUser", null, pageBean);
		return user;
	}

	@Override
	public void saveOrUpdateUser(User user) {
		if(user != null){
			if(user.getUserid() != null){//修改
				user.setUpdate_id(UserContext.getLoginUser().getUserid());
				user.setUpdate_time(new Timestamp(System.currentTimeMillis()));
				userDao.update(user);
			}else{//增加
				user.setCreate_id(UserContext.getLoginUser().getUserid());
				user.setCreate_time(new Timestamp(System.currentTimeMillis()));
				userDao.insert(user);
			}
		}
	}

	@Override
	public void deleteByUserIds(String ids) {
		if(StringUtils.isNotBlank(ids)){
			//userDao.deleteByIds(User.class, ids);
			userDao.deleteByStatment("deleteByUserIds", ids.split(","));
		}
		
	}
	
}
