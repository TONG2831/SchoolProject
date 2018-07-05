package com.test.dao;

import com.test.domain.Admin;

public interface AdminDao {
	public Admin finByName(String username);
	
	public int updatePwd(Admin admin);
	
}
