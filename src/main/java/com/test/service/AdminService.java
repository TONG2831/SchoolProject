package com.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.dao.AdminDao;
import com.test.domain.Admin;

@Service
public class AdminService {

	@Autowired
	AdminDao adminDao;

	public Admin loginValidate(Admin admin) {
		return adminDao.finByName(admin.getUsername());
	}

	/**
	 * 验证原密码 验证成功,修改密码
	 * 
	 * @param username
	 * @return
	 */
	public boolean updatePwd(Admin admin,String newPwd) {
		// 根据用户名获取对象
		Admin admin2 = adminDao.finByName(admin.getUsername());
		// 判断对象是否为空，并判断原密码是否正确，如果正确则修改密码
		if (admin2 != null && admin2.getPassword().equals(admin.getPassword())) {
			admin2.setPassword(newPwd);
			int updatePwd = adminDao.updatePwd(admin2);
			if (updatePwd == 1) {
				return true;
			}
		} else {
			return false;
		}
		return false;
	}
}
