package com.test.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.domain.Admin;
import com.test.service.AdminService;

@Controller
@RequestMapping("/do")
public class AdminController {

	@Autowired
	AdminService adminService;

	@RequestMapping("/main")
	public String loginView() {

		return "redirect:/main.html";
	}

	@RequestMapping(value = "/login")
	@ResponseBody
	public Map<String, String> login(@RequestBody Admin admin, ModelMap model, HttpServletRequest request) {
		System.out.println(admin.toString());

		// 根据用户名获取数据库中账号信息
		Admin admin2 = adminService.loginValidate(admin);
		
		Map<String, String> map = new HashMap<String, String>();
		// 若admin2不为空，且密码相同，则验证成功
		if (admin2 != null && admin2.getPassword().equals(admin.getPassword())) {
			model.addAttribute("username", admin.getUsername());
			HttpSession session = request.getSession();
			session.setMaxInactiveInterval(120 * 60);	//设置session的失效时间两个小时
			session.setAttribute("admin", admin);
		} else {
			map.put("msg", "用户名或密码错误!"); // 失败
		}
		return map;
	}

	@RequestMapping("/getUserName")
	@ResponseBody
	public Map<String, String> getUserName(HttpServletRequest request) {
		Admin admin = (Admin) request.getSession().getAttribute("admin");
		Map<String, String> map = new HashMap<>();
		if (admin != null) {
			map.put("username", admin.getUsername());
		}
		return map;
	}

	/**
	 * 修改密码
	 * 
	 * @param admin
	 * @return
	 */
	@RequestMapping("/alterPwd")
	@ResponseBody
	private Map<String, String> alterPwd(String username, String password, String newPwd) {
		Admin admin = new Admin();
		admin.setUsername(username);
		admin.setPassword(password);
		System.out.println(admin);
		System.out.println(newPwd);
		Map<String, String> map = new HashMap<>();
		boolean updatePwd = adminService.updatePwd(admin, newPwd);
		if (!updatePwd) {
			map.put("msg", "failure");
			return map;
		}
		map.put("msg", "success");
		return map;
	}

}
