package com.test.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.test.domain.StuLogin;
import com.test.domain.Student;
import com.test.service.StuLoginService;
import com.test.service.StudentService;

@Controller
@RequestMapping("/stu")
public class StudentLoginController {

	Logger logger = Logger.getLogger(StudentLoginController.class);

	@Autowired
	StuLoginService stuLoignService;

	@Autowired
	StudentService studentService;

	/**
	 * 登录页面
	 * 
	 * @return
	 */
	@RequestMapping("/login")
	private String toLoginPage() {
		return "redirect:/stulogin.html";
	}

	/**
	 * 跳转至主页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/main")
	private ModelAndView toMainPage(HttpServletRequest request) {
		ModelAndView mView = new ModelAndView();
		
		Object attribute = request.getSession().getAttribute("stuNo");
		if (attribute==null) {
			mView.setViewName("redirect:/stulogin.html");
		}else {
			mView.setViewName("redirect:/stumain.html");
		}
		return mView;
	}

	/**
	 * 登录验证
	 * 
	 * @param username
	 * @param password
	 * @param request
	 * @return
	 */
	@RequestMapping("dologin")
	@ResponseBody
	private String login(String username, String password, HttpServletRequest request) {
		logger.info("student:==username:" + username + "/password:" + password);
		StuLogin stuLogin = new StuLogin();
		stuLogin.setStuNo(username);
		stuLogin.setPassword(password);

		boolean login = stuLoignService.login(stuLogin);
		if (login) {
			HttpSession session = request.getSession();
			session.setMaxInactiveInterval(120 * 60);	//设置session的失效时间两个小时
			session.setAttribute("stuNo", username); // 当前用户的stuNo保存到session中
			return "success";
		} else {
			return "failure";
		}
	}

	/**
	 * 查询学生姓名
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getStuName")
	@ResponseBody
	private Map<String, String> getStuName(HttpServletRequest request) {
		String stuNo = (String) request.getSession().getAttribute("stuNo");
		Student student = studentService.getStudent(stuNo);
		logger.info("登录的学生:"+student);
		Map<String , String> map = new HashMap<>();
		
		if (student != null) {
			map.put("stuName", student.getStuName());
			map.put("stuNo", student.getStuNo());
			return map;
		}
		
		return null;
	}
	
	
	/**
	 * 修改密码
	 * 
	 * @param username
	 * @param oldPwd
	 * @param newPwd
	 * @return 
	 */
	@RequestMapping("/alterPwd")
	@ResponseBody
	private Map<String, String> aletrPwd(String username,String oldPwd,String newPwd ) {
		StuLogin stuLogin = new StuLogin();
		stuLogin.setStuNo(username);
		stuLogin.setPassword(oldPwd);
		
		System.out.println(stuLogin);
		Map<String, String> map = new HashMap<>();
		boolean updatePwd = stuLoignService.updatePwd(stuLogin, newPwd);
		if (!updatePwd) {
			map.put("msg", "failure");
			return map;
		}
		map.put("msg", "success");
		return map;
	}
}
