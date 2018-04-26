package com.encrm.system.service;

import java.util.List;

import com.encrm.publics.util.PageBean;
import com.encrm.system.entity.Subject;
import com.encrm.system.entity.Systemmessage;

public interface ISystemMessageService {

	/**
	 * 查询所有系统消息
	 * @param pageBean
	 * @return
	 */
	public List<Systemmessage> queryAllSystemMessage(PageBean pageBean);
	
	/**
	 * 添加/修改消息
	 * @param systemmessage
	 */
	public void addOrUpdateSystemMessage(Systemmessage systemmessage);
	
	/**
	 * 发布消息
	 * @param systemmessage
	 */
	public void sendMessage(Systemmessage systemmessage);
	
}
