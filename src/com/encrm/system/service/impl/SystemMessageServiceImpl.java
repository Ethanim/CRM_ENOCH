package com.encrm.system.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.encrm.publics.dao.IDataAccess;
import com.encrm.publics.util.PageBean;
import com.encrm.publics.util.UserContext;
import com.encrm.system.entity.Systemmessage;
import com.encrm.system.service.ISystemMessageService;

@Service
@Transactional(rollbackFor=Exception.class)
public class SystemMessageServiceImpl implements ISystemMessageService{
	
	@Autowired
	private IDataAccess<Systemmessage> SystemMessageDao;

	@Override
	public List<Systemmessage> queryAllSystemMessage(PageBean pageBean) {
		List<Systemmessage> systemmessages = SystemMessageDao.queryByStatment("queryAllSystemMessage", null, pageBean);
		return systemmessages;
	}

	@Override
	public void addOrUpdateSystemMessage(Systemmessage systemmessage) {
		if(systemmessage != null){
			if(systemmessage.getSystemmessageId() != null){//修改
				SystemMessageDao.update(systemmessage);
			}else{//添加
				systemmessage.setCreate_id(UserContext.getLoginUser().getUserid());
				systemmessage.setCreate_time(new Timestamp(System.currentTimeMillis()));
				SystemMessageDao.insert(systemmessage);
			}
		}
	}

	@Override
	public void sendMessage(Systemmessage systemmessage) {
		if(systemmessage != null && systemmessage.getSystemmessageId() != null){
			systemmessage.setSend_time(new Timestamp(System.currentTimeMillis()));
			SystemMessageDao.update(systemmessage);
		}
	}

}
