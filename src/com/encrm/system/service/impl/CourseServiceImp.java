package com.encrm.system.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sun.swing.StringUIClientPropertyKey;

import com.encrm.publics.dao.IDataAccess;
import com.encrm.publics.util.PageBean;
import com.encrm.publics.util.UserContext;
import com.encrm.system.entity.Course;
import com.encrm.system.service.ICourseService;

@Service
@Transactional(rollbackFor=Exception.class)
public class CourseServiceImp implements ICourseService{
	
	@Autowired
	private IDataAccess<Course> courseDao;

	@Override
	public List<Course> queryAllCourse(PageBean pageBean) {
		List<Course> courses = courseDao.queryByStatment("queryAllCourse", null, pageBean);
		return courses;
	}

	@Override
	public void saveOrUpdateCourse(Course course) {
		if(course != null){//修改
			if(course.getCourseid() != null){
				course.setUpdate_id(UserContext.getLoginUser().getUserid());
				course.setUpdate_time(new Timestamp(System.currentTimeMillis()));
				courseDao.update(course);
			}else{//添加
				course.setCreate_id(UserContext.getLoginUser().getUserid());
				course.setCreate_time(new Timestamp(System.currentTimeMillis()));
				courseDao.insert(course);
			}
		}
	}

	@Override
	public void deleteCourseById(String courseids) {
		if(StringUtils.isNotBlank(courseids)){
			courseDao.deleteByIds(Course.class, courseids);
		}
	}
	
}
