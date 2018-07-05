package com.test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.service.GradeService;
import com.test.service.PaperAnswerService;

@RequestMapping("/doAnalyze")
@Controller
public class AnalyzeController {
	
	@Autowired
	GradeService gradeService;
	
	@Autowired
	PaperAnswerService paperAnswerService;
	
	@RequestMapping("/analyze")
	@ResponseBody
	private List<String> analyze(String eId,String stuClass,String kind){
		System.out.println(stuClass);
		if ("gradeAnalyze".equals(kind)) {
			return gradeService.analyzeGrade(eId, stuClass);
		}else if ("quesAnalyze".equals(kind)) {
			return paperAnswerService.analyzePaperQues(stuClass, eId);
		}
		return null;
	}                        
}
