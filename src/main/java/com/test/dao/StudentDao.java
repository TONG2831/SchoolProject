package com.test.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.test.domain.Student;

public interface StudentDao {
	
	List<Student> selectByClass(String stuClass);
	
	
	/**
	 * 获取所有院系
	 * 
	 * @return
	 */
	public List<String> getAllStuDepartment();

	/**
	 * 获取学生所在班级
	 * 
	 * @return
	 */
	public List<String> getAllStuClass();
	
	/**
	 * 按院系查询院系包含的班级
	 * 
	 * @param studepartment
	 * @return
	 */
	public List<String>	 getAllClassByDep(String studepartment);
	
	/**
	 * 获取所有学生
	 * 
	 */

	public List<Student> getAllStudent();

	/**
	 * 获取学生信息,根据stuNo查询
	 */
	public Student getStudent(String stuNo);

	/**
	 * 多条件查询学生信息
	 * 
	 * @param stuClass
	 * @param stuDepartment
	 * @return
	 */
	public List<Student> selectStuByConds(@Param("stuClass") String stuClass,
			@Param("stuDepartment") String stuDepartment, @Param("searchCond") String searchCond,
			@Param("searchText") String searchText);

	/**
	 * 单个添加
	 */
	public void addStudent(Student student);

	/**
	 * 修改学生信息
	 * 
	 * @param student
	 */
	public void updateStudent(Student student);

	/**
	 * 删除
	 * 
	 * @param stuNo
	 */
	public void deleteStudent(String stuNo);
}
