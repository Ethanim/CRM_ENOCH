package com.encrm.system.service;

import java.util.List;

import com.encrm.publics.util.PageBean;
import com.encrm.system.entity.Course;
import com.encrm.system.entity.Subject;

public interface ISubjectService {
	/**
	 * 查询所有科目
	 * @param pageBean
	 * @return
	 */
	public List<Subject> queryAllSubject(PageBean pageBean);
	
	/**
	 * 添加/修改科目
	 * @param subject
	 */
	public void addOrUpdateSubject(Subject subject);
	
	/**
	 * 根据 ids 删除科目
	 * @param ids
	 */
	public void deleteSubjectById(String ids);
}
