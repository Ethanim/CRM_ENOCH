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
import com.encrm.system.entity.Course;
import com.encrm.system.entity.Dept;
import com.encrm.system.service.ICourseService;

@Controller
public class CourseController extends BaseController{
	
	@Autowired
	private ICourseService courseService;
	
	/**
	 * 跳转课程界面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/system/courseMang.do", method=RequestMethod.GET)
	public String courseManager(Model model){
		if(UserContext.getLoginUser() != null){
			return JumpViewConstants.SYSTEM_COURSE;
		}
		return JumpViewConstants.SYSTEM_LOGIN;
	}
	
	/**
	 * 显示课程列表
	 * @param request
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="/course/queryCourse.do", method=RequestMethod.GET)
	public @ResponseBody String queryAllCourse(HttpServletRequest request, Integer currentPage, Integer pageSize){
		List<Course> courses = courseService.queryAllCourse(processPageBean(pageSize, currentPage));
		return jsonToPage(courses);
	}
	
	/**
	 * 添加或修改课程
	 * @param request
	 * @param course
	 * @return
	 */
	@RequestMapping(value="/course/addCourse.do", method=RequestMethod.POST)
	public @ResponseBody String saveOrUpdateCourse(HttpServletRequest request, Course course){
		if(course != null){
			courseService.saveOrUpdateCourse(course);
			return ReturnConstants.SUCCESS;
		}
		return ReturnConstants.PARAM_NULL;
	}
	
	/**
	 * 删除课程
	 * @param request
	 * @param courseids
	 * @return
	 */
	@RequestMapping(value="/course/deleteCourse.do", method=RequestMethod.POST)
	public @ResponseBody String deleteCourseById(HttpServletRequest request, String courseids){
		if(StringUtils.isNotBlank(courseids)){
			//由于前台传输的数据，有问题，这里把数据改回正常数据，在正常操作
			//前台传输数据：21 ng-dirty ng-touched,22 ng-dirty ng-touched 改为： 21,22
			String res = "";
			String[] str = courseids.split(",");
			for(int i=0; i<str.length; i++){
				String[] ss = str[i].split(" ");
				if(i == str.length - 1){
					res = res + ss[0];
				}else{
					res = res + ss[0] + ",";
				}
			}
			courseService.deleteCourseById(res);
			return ReturnConstants.SUCCESS;
		}
		return ReturnConstants.PARAM_NULL;
	}
}
