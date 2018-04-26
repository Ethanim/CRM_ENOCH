package com.encrm.system.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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
import com.encrm.system.entity.Subject;
import com.encrm.system.service.ISubjectService;

@Controller
public class SubjectController extends BaseController{
	
	@Autowired
	private ISubjectService subjectService;
	
	/**
	 * 跳转科目页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/system/subjectMang.do", method=RequestMethod.GET)
	public String subjectManager(Model model){
		if(UserContext.getLoginUser() != null){
			return JumpViewConstants.SYSTEM_SUBJECT;
		}
		return JumpViewConstants.SYSTEM_LOGIN;
	}
	
	/**
	 * 显示科目列表
	 * @param request
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="/subject/querySubject.do", method=RequestMethod.GET)
	public @ResponseBody String queryAllSubject(HttpServletRequest request, Integer currentPage, Integer pageSize){
		List<Subject> subjects = subjectService.queryAllSubject(processPageBean(pageSize, currentPage));
		return jsonToPage(subjects);
	}
	
	/**
	 * 添加/修改学科
	 * @param request
	 * @param subject
	 * @return
	 */
	@RequestMapping(value="/subject/addOrUpdateSubject.do", method=RequestMethod.POST)
	public @ResponseBody String addOrUpdateSubject(HttpServletRequest request, Subject subject){
		if(subject != null){
			subjectService.addOrUpdateSubject(subject);
			return ReturnConstants.SUCCESS;
		}
		return ReturnConstants.PARAM_NULL;
	}
	
	/**
	 * 删除学科
	 * @param request
	 * @param subjectids
	 * @return
	 */
	@RequestMapping(value="/subject/deleteSubject.do", method=RequestMethod.POST)
	public @ResponseBody String deleteSubject(HttpServletRequest request, String subjectids){
		if(StringUtils.isNotBlank(subjectids)){
			subjectService.deleteSubjectById(subjectids); 
			return ReturnConstants.SUCCESS;
		}
		return ReturnConstants.PARAM_NULL;
	}
}
