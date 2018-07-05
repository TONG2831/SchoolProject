package com.test.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.test.domain.Paper;
import com.test.domain.Question;
import com.test.domain.QuestionInfo;
import com.test.service.QuesAndPaperService;
import com.test.service.QuestionService;

/**
 * 试题控制层
 * 
 * @author lt
 *
 */
@Controller
@RequestMapping("/doQues")
public class QuestionController {
	
	private Logger logger = Logger.getLogger(QuestionController.class);
	
	@Autowired
	QuestionService questionService;
	
	@Autowired
	QuesAndPaperService quesAndPaperService;
	
	/**
	 * 分页获取所有试题信息,使用的是左外连接
	 * 
	 * @param pageNo
	 * @param qType
	 * @param pName
	 * @param searchText
	 * @return
	 */
	@RequestMapping("/getAllQInfo")
	@ResponseBody
	public PageInfo<QuestionInfo> getQuestionInfo(String pageNo,String qType,String pName,String searchText) {
		System.out.println("PageNo:-----"+pageNo);
		System.out.println("查询条件:==="+"qType:"+qType+" - pName:"+pName+" - searchText:"+searchText.trim());
		 return questionService.getQuestionInfo(Integer.valueOf(pageNo),qType,pName,searchText.trim());
	}
	
	/**
	 * 获取所有书卷信息,用以填充下拉菜单
	 * 
	 * @return
	 */
	@RequestMapping("/getPInfo")
	@ResponseBody
	public List<Paper> getAllDepartment() {
		return questionService.getAllPaper();
		
	}
	
	/**
	 * 获取单个试题信息
	 * 
	 * @param qId
	 * @return
	 */
	@RequestMapping("/getOneQInfo")
	@ResponseBody
	public Question getOneQInfo(Integer qId){
		return questionService.getOneQestion(qId);
	}
	
	/**
	 * 修改试题
	 * 
	 * @param question
	 * @return
	 */
	@RequestMapping("/alterQues")
	@ResponseBody
	public Map<String, String> alterQues(@RequestBody Question question) {
		logger.info("---修改试题:"+question);
		questionService.alterQues(question);
		Map<String, String> map = new HashMap<>();
		map.put("msg", "success");
		return map;
	}
	
	/**
	 * 删除试题
	 * 
	 * @param qId
	 */
	@RequestMapping("/deleteQues")
	@ResponseBody
	public void deleteQues(Integer qId){
		System.out.println(qId);
		questionService.deleteQues(qId);
	}
	
	/**
	 * 批量删除
	 * 
	 * @param qId
	 */
	@RequestMapping("/deleteSome")
	@ResponseBody
	public void deleteSome(String qId) {
		System.out.println("待删除的qId:"+qId);
		String[] split = qId.split("\\.");
		for (int i = 0; i < split.length; i++) {
			questionService.deleteQues(Integer.valueOf(split[i]));
		}
		
	}
	
	/**
	 * 添加试题
	 * 
	 * @param question
	 * @return
	 */
	@RequestMapping("/addQues")
	@ResponseBody
	public Map<String,String> addQues(@RequestBody Question question) {
		questionService.addQues(question);
		System.out.println(question);
		Map<String, String> map = new HashMap<>();
		map.put("msg", "success");
		return map;
	}
	
	
	
	
	/**
	 * 获取当前最大主键
	 * 
	 * @return
	 */
	@RequestMapping("/getQId")
	@ResponseBody
	public Map<String, Integer> getQId() {
		Integer qId = questionService.getQId()+1;
		Map<String, Integer> map = new  HashMap<>();
		map.put("qId", qId);
		return map;
	}
	
	/**
	 * 单个删除
	 * 
	 * 删除试题与试卷之间的联系
	 * 
	 * @param qId
	 * @param pId
	 */
	@RequestMapping("/deleteQuesAndPaper")
	@ResponseBody
	private void deleteQuesAndPaper(Integer qId,Integer pId) {
		quesAndPaperService.deletQuesAndPaper(qId, pId);
		
	}
	
	/**
	 * 批量删除
	 * 
	 * 删除试题与试卷之间的联系
	 * 
	 * @param qId
	 * @param pId
	 */
	@RequestMapping("/deleteSomeQuesAndPaper")
	@ResponseBody
	private void deleteSomeQuesAndPaper(String qId,Integer pId) {
		quesAndPaperService.deletSomeQuesAndPaper(qId, pId);
	}
	
	
	/**
	 * 添加试题与试卷之间的联系
	 * 
	 * @param qId
	 * @param pId
	 */
	@RequestMapping("/addQuesAndPaper")
	@ResponseBody
	private void addQuesAndPaper(String qId,Integer pId) {
		logger.info("qId:==="+qId+"/pId:=="+pId);
		quesAndPaperService.addQuesAndPaper(qId, pId);
	}
	
}
