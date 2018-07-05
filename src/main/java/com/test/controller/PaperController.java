package com.test.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.test.domain.Paper;
import com.test.service.PaperService;

@Controller
@RequestMapping("/doPaper")
public class PaperController {
	
	@Autowired
	PaperService pService;
	
	/**
	 * 分页获取试卷信息
	 * 
	 * @param pageNo
	 * @param searchText
	 * @return
	 */
	@RequestMapping("/getPaper")
	@ResponseBody
	public PageInfo<Paper> getPaper(int pageNo,String searchText){
		PageInfo<Paper> paper = pService.getPaper(pageNo,searchText.trim());
		return paper;
	}
	
	/**
	 * 创建试卷
	 * 
	 * @param pName
	 * @param qId
	 */
	@RequestMapping("/addPaper")
	@ResponseBody
	public String addPaper(String pName,String qId) {
		System.out.println(pName+qId);
		int addPaper = pService.addPaper(pName, qId);
		return addPaper+"";
	}
	
	/**
	 * 自动生成试卷
	 * 
	 */
	@RequestMapping("/autoCreate")
	@ResponseBody
	private String autoCreatePaper(String pName,Integer cNum,Integer dcNum,Integer jNum,Integer fNum,Integer aNum) {
		int auto = pService.autoCreatePaper(pName, cNum, dcNum, jNum, fNum, aNum);
		return auto+"";
	}
	
	
	/**
	 * 删除试题
	 * 
	 * @param pId
	 */
	@RequestMapping("/deletePaper")
	@ResponseBody
	public void deletePaper(Integer pId){
		System.out.println(pId);
		pService.deletePaper(pId);
	}
	
	/**
	 * 批量删除
	 * 
	 * @param pId
	 */
	@RequestMapping("/deleteQuesAndPaper")
	@ResponseBody
	public void deleteSome(String pId) {
		pService.deleteSome(pId);
	}
	
	/* 修改试卷,对试卷的题目做增加和删除*/
	
	/**
	 * 保存当前选中的试卷Id
	 * 
	 * @param pId
	 * @param request
	 */
	@RequestMapping("/savePId")
	@ResponseBody
	public void savePId(String pId,HttpServletRequest request){
		request.getSession().setAttribute("pId", pId);
	}
	
	
	/**
	 * 获取当前选中的id
	 * 更具当前id获取试卷信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getPId")
	@ResponseBody
	public Paper  getPId(HttpServletRequest request){
		String pId = (String) request.getSession().getAttribute("pId");
		Paper selectPaperByPId = pService.selectPaperByPId(pId);
		return selectPaperByPId;
	}
	
}
