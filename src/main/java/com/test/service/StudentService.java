package com.test.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.test.dao.StuLoginMapper;
import com.test.dao.StudentDao;
import com.test.domain.StuLogin;
import com.test.domain.Student;

@Service
public class StudentService {

	@Autowired
	StudentDao studentDao;
	
	@Autowired
	StuLoginMapper stuLoginMapper;
	
	/**
	 * 分页查询
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public PageInfo<Student> getStudentByPage(int pageNo, int pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		List<Student> students = studentDao.getAllStudent();
		return new PageInfo<Student>(students);

	}

	/**
	 * 获取所有院系名称
	 * 
	 * @return
	 */
	public List<String> getAllDepartment() {
		List<String> allStuDepartment = studentDao.getAllStuDepartment();
		return allStuDepartment;
	}
	
	/**
	 * 按院系查询班级名
	 * 
	 * @param stuDepartment
	 * @return
	 */
	public List<String> getAllClassByDep(String stuDepartment){
		List<String> allClassByDep = studentDao.getAllClassByDep(stuDepartment);
		return allClassByDep;
	}
	
	
	/**
	 * 获取所有班级名称
	 * 
	 * @return
	 */
	public List<String> getAllClass() {
		List<String> allStuClass = studentDao.getAllStuClass();
		return allStuClass;
	}

	/**
	 * 获取所有学生信息
	 * 
	 * @return
	 */
	public List<Student> getAllStudent() {
		List<Student> allStudent = studentDao.getAllStudent();
		return allStudent;
	}

	/**
	 * 添加学生
	 * 添加学生信息后
	 * 给学生添加登录账号密码
	 * @param student
	 */
	public void addStudent(Student student) {
		studentDao.addStudent(student);
		StuLogin stuLogin = new StuLogin();
		stuLogin.setStuNo(student.getStuNo());
		stuLogin.setPassword(student.getStuNo());
		stuLoginMapper.insertSelective(stuLogin);
	}

	/**
	 * 按条件查询学生信息
	 * 
	 * @param stuNo
	 * @return
	 */
	public Student getStudent(String stuNo) {
		Student student = studentDao.getStudent(stuNo);
		return student;

	}

	/**
	 * 修改学生信息
	 * 
	 * @param student
	 */
	public void alterStudent(Student student) {
		studentDao.updateStudent(student);
	}

	/**
	 * 删除学生
	 * 
	 * @param stuNo
	 */
	public void deleteStduent(String stuNo) {
		stuLoginMapper.deleteByPrimaryKey(stuNo);
		studentDao.deleteStudent(stuNo);
	}

	/**
	 * 多条件查询学生信息
	 * 
	 * @param stuClass
	 * @param stuDepartment
	 * @return
	 */
	public PageInfo<Student> selectStuByConds(Integer pageNo, Integer pageSize, String stuClass, String stuDepartment,String cond,String text) {
		PageHelper.startPage(pageNo, pageSize);
		List<Student> students = studentDao.selectStuByConds(stuClass, stuDepartment,cond,text);
		return new PageInfo<Student>(students);
	}
}
