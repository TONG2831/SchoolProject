package com.test.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.test.domain.Student;
import com.test.service.StudentService;

@Controller
@RequestMapping("/doStu")
public class StudentController {
	Logger log = Logger.getLogger(StudentController.class);

	// 定义页面大小常量
	private static final int pageSize = 8;

	@Autowired
	StudentService studentService;

	// 获取所有院系
	@RequestMapping("/getDep")
	@ResponseBody
	public List<String> getAllDepartment() {
		List<String> allDepartment = studentService.getAllDepartment();
		System.out.println(allDepartment);
		return allDepartment;
	}

	// 按照院系获取班级名称
	@RequestMapping("/getClassByDep")
	@ResponseBody
	public List<String> getAllClassByDep(String stuDepartment) {
		List<String> allClass = studentService.getAllClassByDep(stuDepartment);
		System.out.println(allClass);
		return allClass;
	}

	// 获取所有班级名称
	@RequestMapping("/getClass")
	@ResponseBody
	public List<String> getAllClass() {
		List<String> allClass = studentService.getAllClass();
		System.out.println(allClass);
		return allClass;
	}

	// 获取所有学生信息
	@RequestMapping("/getStus")
	@ResponseBody
	public List<Student> getAllStudent() {
		List<Student> allStudent = studentService.getAllStudent();
		System.out.println(allStudent);
		return allStudent;
	}

	// 单个添加学生
	@RequestMapping("/addStu")
	@ResponseBody
	public Map<String, String> addStudent(@RequestBody Student student) {
		log.info("------------添加学生开始--------------" + student);
		String msg = null;
		// 先验证该学号是否存在
		Student stu = studentService.getStudent(student.getStuNo());
		if (stu == null) {
			msg = "0";
			studentService.addStudent(student);
		} else {
			msg = "1";
		}

		log.info("------------添加学生------END--------");

		Map<String, String> map = new HashMap<>();
		map.put("msg", msg);
		return map;
	}

	// 按学号查学生信息
	@RequestMapping("/getStu")
	@ResponseBody
	public Student getStudent(String stuNo) {
		System.out.println(stuNo);
		Student student = studentService.getStudent(stuNo);
		return student;
	}

	// 多条件查询学生信息
	@RequestMapping("/selStuByConds")
	@ResponseBody
	public PageInfo<Student> getStudentByConds(String searchCond, String searchText, String pageNo,
			@RequestParam("stuClass") String stuClass, @RequestParam("stuDepartment") String stDepartment) {
		log.info("---------多条件查询学生信息-----BEGIN----");
		log.info("---------stuClass = " + stuClass + "-------stuDepartment = " + stDepartment);
		PageInfo<Student> students = studentService.selectStuByConds(Integer.valueOf(pageNo), pageSize, stuClass,
				stDepartment, searchCond, searchText.trim());
		return students;
	}

	// 分页
	@RequestMapping("/getStuPage")
	@ResponseBody
	public PageInfo<Student> selectByPage(String pageNo) {
		log.info("pageNo:-------------" + pageNo);
		PageInfo<Student> studentByPage = studentService.getStudentByPage(Integer.valueOf(pageNo), pageSize);
		return studentByPage;

	}

	// 修改学生信息
	@RequestMapping("/alterStu")
	@ResponseBody
	public Map<String, String> alterStduent(@RequestBody Student student) {
		log.info("-------------修改学生信息:" + student);
		studentService.alterStudent(student);
		log.info("-------------修改学生信息-------END---");
		Map<String, String> map = new HashMap<>();
		map.put("msg", "success");
		return map;
	}

	// 单个删除
	@RequestMapping("/deleteStu")
	@ResponseBody
	public void deleteStudent(String stuNo) {
		log.info("开始删除:---------" + stuNo + "--------");
		studentService.deleteStduent(stuNo);
		log.info("删除--------------END");
	}

	// 批量删除
	@RequestMapping("/deleteSome")
	@ResponseBody
	public void deleteSome(String stuNo) {
		System.out.println(stuNo);
		String[] stuNos = stuNo.split("/");
		for (int i = 0; i < stuNos.length; i++) {
			studentService.deleteStduent(stuNos[i]);
		}
	}
}
