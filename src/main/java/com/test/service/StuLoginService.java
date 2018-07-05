package com.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.dao.StuLoginMapper;
import com.test.domain.StuLogin;

@Service
public class StuLoginService {
	
	@Autowired
	StuLoginMapper stuLoginMapper;
	
	/**
	 * 学生登录验证
	 * 
	 * @param stuLogin
	 * @return
	 */
	public boolean login(StuLogin stuLogin){
		StuLogin stu = stuLoginMapper.selectByPrimaryKey(stuLogin.getStuNo());
		if (stu!=null&&stu.getStuNo().equals(stuLogin.getStuNo())) {
			if (stu.getPassword().equals(stuLogin.getPassword())) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * 修改密码
	 * 
	 * @param stuLogin
	 * @param newPwd
	 * @return
	 */
	public boolean updatePwd(StuLogin stuLogin,String newPwd) {
		StuLogin stu1 = stuLoginMapper.selectByPrimaryKey(stuLogin.getStuNo());
		System.out.println("stu1:"+stu1);
		if (stu1 != null && stu1.getPassword().equals(stuLogin.getPassword())) {
			stu1.setPassword(newPwd);
			int updatePwd = stuLoginMapper.updateByPrimaryKeySelective(stu1);
			if (updatePwd == 1) {
				return true;
			}
		} else {
			return false;
		}
		return false;
	}
}
