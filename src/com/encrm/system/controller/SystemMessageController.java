package com.encrm.system.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.encrm.publics.constants.JumpViewConstants;
import com.encrm.publics.constants.ReturnConstants;
import com.encrm.publics.util.BaseController;
import com.encrm.publics.util.UserContext;
import com.encrm.system.entity.Systemmessage;
import com.encrm.system.service.ISystemMessageService;

@Controller
public class SystemMessageController extends BaseController{
	
	@Autowired
	private ISystemMessageService systemMessageService;
	
	/**
	 * 跳转系统消息界面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/system/systemMessage.do", method=RequestMethod.GET)
	public String systemMessageManager(Model model){
		if(UserContext.getLoginUser() != null){
			return JumpViewConstants.SYSTEM_MESSAGE;
		}
		return JumpViewConstants.SYSTEM_LOGIN;
	}
	
	/**
	 * 查询系统消息列表
	 * @param request
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="/system/querySystemmessages.do", method=RequestMethod.GET)
	public @ResponseBody String queryAllSystemMessage(HttpServletRequest request, Integer currentPage, Integer pageSize){
		List<Systemmessage> systemMessages = systemMessageService.queryAllSystemMessage(processPageBean(pageSize, currentPage));
		return jsonToPage(systemMessages);
	}
	
	/**
	 * 添加/修改系统消息
	 * @param request
	 * @param systemmessage
	 * @return
	 */
	@RequestMapping(value="/system/saveOrUpdateMessage.do", method=RequestMethod.POST)
	public @ResponseBody String addOrUpdateSystemMessage(HttpServletRequest request, Systemmessage systemmessage){
		if(systemmessage != null){
			systemMessageService.addOrUpdateSystemMessage(systemmessage);
			return ReturnConstants.SUCCESS;
		}
		return ReturnConstants.PARAM_NULL;
	}
	
	/**
	 * 消息发布与撤回
	 * @param request
	 * @param systemmessage
	 * @return
	 */
	@RequestMapping(value="/system/sendMessage.do", method=RequestMethod.GET)
	public @ResponseBody String sendMessage(HttpServletRequest request, Systemmessage systemmessage){
		if(systemmessage != null){
			systemMessageService.sendMessage(systemmessage);
			return ReturnConstants.SUCCESS;
		}
		return ReturnConstants.PARAM_NULL;
	}
}
