package com.encrm.system.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.encrm.publics.constants.ReturnConstants;
import com.encrm.publics.dao.IDataAccess;
import com.encrm.publics.util.PageBean;
import com.encrm.publics.util.UserContext;
import com.encrm.system.entity.Role;
import com.encrm.system.service.IRoleService;

@Service
public class RoleServiceImpl implements IRoleService{
	
	@Autowired
	private IDataAccess<Role> roleDao;

	@Override
	public List<Role> queryAllRole(PageBean pageBean) {
		List<Role> list = roleDao.queryByStatment("queryAllRole", null, pageBean);
		return list;
	}

	@Override
	public void addOrUpdateRole(Role role) {
		if(role != null){
			if(role.getRoleid() != null){//修改
				role.setUpdate_id(UserContext.getLoginUser().getUserid());
				role.setUpdate_time(new Timestamp(System.currentTimeMillis()));
				roleDao.update(role);
			}else{//添加
				role.setCreate_id(UserContext.getLoginUser().getUserid());
				role.setCreate_time(new Timestamp(System.currentTimeMillis()));
				roleDao.insert(role);
			}
		}
	}

	@Override
	public void deleteByRoleIds(String ids) {
		if(StringUtils.isNotBlank(ids)){
			roleDao.deleteByIds(Role.class, ids);
		}
	}

}
