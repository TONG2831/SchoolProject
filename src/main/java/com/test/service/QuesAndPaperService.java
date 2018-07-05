package com.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.dao.QuestionAndPaperMapper;
import com.test.domain.QuestionAndPaper;

@Service
public class QuesAndPaperService {
	
	@Autowired
	QuestionAndPaperMapper questionAndPaperMapper;
	
	/**
	 * 单个删除
	 * 
	 * 删除试题和试卷之间的联系
	 * 
	 * @param qId
	 * @param pId
	 */
	public void deletQuesAndPaper(Integer qId,Integer pId) {
		int deleteByQIdAndPId = questionAndPaperMapper.deleteByQIdAndPId(qId, pId);
		System.out.println("删除deleteByQIdAndPId:"+deleteByQIdAndPId);
	}
	
	
	/**
	 * 批量删除
	 * 
	 * @param qId
	 * @param pId
	 */
	public void deletSomeQuesAndPaper(String qId, Integer pId){
		String[] split = qId.split("\\.");
		for (int i = 0; i < split.length; i++) {
			deletQuesAndPaper(Integer.valueOf(split[i]), pId);
		}
	}
	
	
	
	/**
	 * 添加试题和试卷之间的联系
	 * 
	 * @param qId
	 * @param pId
	 */
	public void addQuesAndPaper(String qId,Integer pId) {
		String[] split = qId.split("\\.");
		QuestionAndPaper questionAndPaper = new QuestionAndPaper();
		for (int i = 0; i < split.length; i++) {
			System.out.println(split[i]);
			questionAndPaper.setpId(pId);
			questionAndPaper.setqId(Integer.valueOf(split[i]));
			int addQuesAndPaper = questionAndPaperMapper.insertSelective(questionAndPaper);
			System.out.println("添加addQuesAndPaper:"+addQuesAndPaper);
		}
		
	}
	
}
