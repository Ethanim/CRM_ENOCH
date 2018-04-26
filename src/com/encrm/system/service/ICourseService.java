package com.encrm.system.service;

import java.util.List;

import com.encrm.publics.util.PageBean;
import com.encrm.system.entity.Course;

public interface ICourseService {
	/**
	 * 查询所有课程
	 * @param pageBean
	 * @return
	 */
	public List<Course> queryAllCourse(PageBean pageBean);
	
	/**
	 * 添加/修改课程
	 * @param course
	 */
	public void saveOrUpdateCourse(Course course);
	
	/**
	 * 删除课程
	 * @param course
	 */
	public void deleteCourseById(String courseids);
}
