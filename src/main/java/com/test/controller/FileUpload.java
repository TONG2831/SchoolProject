package com.test.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.test.domain.PaperInfo;
import com.test.domain.Student;
import com.test.service.FileService;
import com.test.service.StudentService;
import com.test.util.FileUtils;

@Controller
@RequestMapping("/doFile")
public class FileUpload {
	
	@Autowired
	StudentService studentService;
	
	@Autowired
	FileService fileService;
	
	/**
	 * 学生名单本地导入
	 * 
	 * @param file
	 * @param request
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@RequestMapping("/uploadStu")
	@ResponseBody
	public Map<String, String> uploadFile(MultipartFile file, HttpServletRequest request) throws IllegalStateException, IOException {
		// 获取文件名
		String fileName = file.getOriginalFilename();
		String path = request.getServletContext().getRealPath("/temp");
		
		// 封装成file对象,上传至服务器
		File dir = new File(path, fileName);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		file.transferTo(dir);// MultipartFile自带的解析方法
		
		// 调用解析方法
		List<Student> students = FileUtils.analyzeExcel(dir);
		System.out.println("学生人数共计:==="+students.size());
		
		// 开始保存学生信息
		List<Student> allStudent = studentService.getAllStudent();
		
		List<Student> existStu = new LinkedList<>();
		for (Student student : students) {
			if (allStudent.contains(student)) {
				existStu.add(student);
			}
		}
		
		students.removeAll(existStu);
		System.out.println("去除已经存在的student:"+students.size()+"---"+existStu.size());
		
		for (Student student : students) {
			studentService.addStudent(student);
		}
		Map<String, String> map = new HashMap<>();
		String  msg = "成功导入"+students.size()+"条数据,已存在"+existStu.size()+"人";
		map.put("msg", msg);
		return map;
		
	}
	
	
	
	/**
	 * 试卷的本地导入
	 * 
	 * @param file
	 * @param request
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@RequestMapping("/uploadPaper")
	@ResponseBody
	public Map<String, String> uploadWord(MultipartFile file, HttpServletRequest request) throws IllegalStateException, IOException {
		// 获取文件名
		String fileName = file.getOriginalFilename();
		String path = request.getServletContext().getRealPath("/temp");
		
		// 封装成file对象,上传至服务器
		File dir = new File(path, fileName);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		file.transferTo(dir);// MultipartFile自带的解析方法
		
		int addPape = -1;
		// 调用解析方法
		try {
			PaperInfo analyzeWord = FileUtils.analyzeWord(dir);
			addPape = fileService.addPaper(analyzeWord);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Map<String, String> map = new HashMap<>();
		String  msg = addPape+"";
		map.put("msg", msg);
		return map;
		
	}
}
