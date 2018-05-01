package com.encrm.system.service.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.encrm.publics.dao.IDataAccess;
import com.encrm.publics.util.PageBean;
import com.encrm.publics.util.UserContext;
import com.encrm.system.entity.Dept;
import com.encrm.system.entity.Role;
import com.encrm.system.service.IDeptService;

@Service
@Transactional(rollbackFor=Exception.class)
public class DeptServiceImpl implements IDeptService {

	@Autowired
	private IDataAccess<Dept> deptDao;
	
	@Autowired
	private IDataAccess<Role> roleDao;
	
	//@Cacheable(value="baseCache",key="'queryAllDepts'")
	@Override
	public List<Dept> queryAllDepts(PageBean pageBean) {
		List<Dept> list = deptDao.queryByStatment("queryAllDepts", null, pageBean);
		return list;
	}

	@Override
	public List<Role> queryRolesByDeptId(String deptid) {
		Map<String, Object> param = new HashMap<>();
		param.put("deptid", deptid);
		List<Role> list = roleDao.queryByStatment("queryRolesByDeptId", param, null);
		return list;
	}

	@Override
	public void saveOrUpdateDept(Dept dept) {
		if(dept != null){
			if(dept.getDeptid() != null){//修改
				dept.setUpdate_id(UserContext.getLoginUser().getUserid());
				dept.setUpdate_time(new Timestamp(System.currentTimeMillis()));
				deptDao.update(dept);
			}else{//删除
				dept.setCreate_id(UserContext.getLoginUser().getUserid());
				dept.setCreate_time(new Timestamp(System.currentTimeMillis()));
				deptDao.insert(dept);
			}
		}
	}

	@Override
	public void deleteDeptById(String ids) {
		if(StringUtils.isNotBlank(ids)){
			deptDao.deleteByIds(Dept.class, ids);
		}
	}
}
