package com.encrm.system.service.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.encrm.publics.dao.IDataAccess;
import com.encrm.publics.util.UserContext;
import com.encrm.system.entity.Menu;
import com.encrm.system.entity.Role_menu;
import com.encrm.system.service.IMenuService;

@Service
public class MenuServiceImpl implements IMenuService {
	
	@Autowired
	private IDataAccess<Menu> menuDao;
	
	@Autowired
	private IDataAccess<Role_menu> rmDao;

	@Cacheable(value="baseCache",key="'queryAllMenu'")
	@Override
	public List<Menu> queryAllMenu() {
		Map<String, Object> param = new HashMap<>();
		param.put("isparid", "true");
		List<Menu> list = menuDao.queryByStatment("queryAllMenu", param, null);
		if(list != null && list.size() > 0){
			for(int i=0; i<list.size(); i++){
				Long menuid = list.get(i).getMenuid();
				param.clear();
				param.put("isparid", null);
				param.put("menuparaid", menuid);
				List<Menu> listChildren = menuDao.queryByStatment("queryAllMenu", param, null);
				list.get(i).setChildren(listChildren);
			}
		}
		return list;
	}

	@Override
	public List<Menu> queryAlreadySelectedMenu(String roleid) {
		Map<String, Object> param = new HashMap<>();
		param.put("roleid", roleid);
		List<Menu> menus = menuDao.queryByStatment("queryAlreadySelectedMenu", param, null);
		return menus;
	}

	@Override
	public void deleteRoleMenu(String roleid) {
		if(StringUtils.isNotBlank(roleid)){
			Map<String, Object> param = new HashMap<>();
			param.put("roleid", roleid);
			menuDao.deleteByStatment("deleteRoleMenu", param);
		}
	}

	@Override
	public void saveRoleMenu(Role_menu menu) {
		if(menu != null){
			rmDao.insert(menu);
		}
	}

	@Override
	public void addOrUpdateMenu(Menu menu) {
		if(menu != null){
			if(menu.getMenuid() != null){//修改
				menu.setUpdate_id(UserContext.getLoginUser().getUserid());
				menu.setUpdate_time(new Timestamp(System.currentTimeMillis()));
				menuDao.update(menu);
			}else{//添加
				menu.setCreate_id(UserContext.getLoginUser().getUserid());
				menu.setCreate_time(new Timestamp(System.currentTimeMillis()));
				menuDao.insert(menu);
			}
		}
	}

	@Override
	public boolean isHasChildrenMenu(String id) {
		Map<String, Object> param = new HashMap<>();
		param.put("id",id);
		List<Menu> menus = menuDao.queryByStatment("isHasChildrenMenu", param, null);
		if(menus != null && menus.size() > 0){
			return true;
		}
		return false;
	}

	@Override
	public boolean isHasRoleMenu(String id) {
		Map<String, Object> param = new HashMap<>();
		param.put("id",id);
		List<Menu> menus = menuDao.queryByStatment("isHasRoleMenu", param, null);
		if(menus != null && menus.size() > 0){
			return true;
		}
		return false;
	}

	@Override
	public Menu queryMenuById(String id) {
		if(StringUtils.isNotEmpty(id)){
			Map<String, Object> param = new HashMap<>();
			param.put("id",id);
			List<Menu> menus = menuDao.queryByStatment("queryMenuById", param, null);
			if(menus != null && menus.size() > 0){
				return menus.get(0);
			}
		}
		return null;
	}

	@Override
	public void deleteMenuById(String ids) {
		if(StringUtils.isNotEmpty(ids)){
			menuDao.deleteByIds(Menu.class, ids);
		}
	}

}
