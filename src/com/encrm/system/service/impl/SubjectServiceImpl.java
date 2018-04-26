package com.encrm.system.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.encrm.publics.dao.IDataAccess;
import com.encrm.publics.util.PageBean;
import com.encrm.publics.util.UserContext;
import com.encrm.system.entity.Course;
import com.encrm.system.entity.Subject;
import com.encrm.system.service.ICourseService;
import com.encrm.system.service.ISubjectService;

@Service
public class SubjectServiceImpl implements ISubjectService{
	
	@Autowired
	private IDataAccess<Subject> subjectDao;

	@Override
	public List<Subject> queryAllSubject(PageBean pageBean) {
		List<Subject> subjects = subjectDao.queryByStatment("queryAllSubject", null, pageBean);
		return subjects;
	}

	@Override
	public void addOrUpdateSubject(Subject subject) {
		if(subject != null){
			if(subject.getSubjectid() != null){//修改
				subject.setUpdate_id(UserContext.getLoginUser().getUserid());
				subject.setUpdate_time(new Timestamp(System.currentTimeMillis()));
				subjectDao.update(subject);
			}else{//添加
				subject.setCreate_id(UserContext.getLoginUser().getUserid());
				subject.setCreate_time(new Timestamp(System.currentTimeMillis()));
				subjectDao.insert(subject);
			}
		}
	}

	@Override
	public void deleteSubjectById(String ids) {
		if(StringUtils.isNotBlank(ids)){
			subjectDao.deleteByIds(Subject.class, ids);
		}
	}
	
}
