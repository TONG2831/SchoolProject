package com.test.controller;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.test.domain.Grade;
import com.test.dto.ExamInfo;
import com.test.dto.QuestionMarkList;
import com.test.dto.StuGrade;
import com.test.service.ExamService;
import com.test.service.GradeService;
import com.test.service.PaperAnswerService;
import com.test.util.ExportToExcel;

/**
 * 阅卷
 * 
 * @author lt
 *
 */
@Controller
@RequestMapping("/doMark")
public class MarkController {

	@Autowired
	GradeService gradeService;

	@Autowired
	ExamService examService;

	@Autowired
	PaperAnswerService paperAnswerService;

	/**
	 * @param eId
	 *            试卷id
	 * @param stuClass
	 *            班级
	 * @param pageNo
	 *            页数
	 * @param searchCond
	 *            搜索条件
	 * @param searchText
	 *            搜索关键字
	 */
	@RequestMapping("/getPaperAnswer")
	@ResponseBody
	private PageInfo<StuGrade> getPaperAnswer(String eId, String stuClass, String pageNo, String searchCond,
			String searchText) {
		PageInfo<StuGrade> gradeByPage = gradeService.getGradeByPage(eId, stuClass, pageNo, searchCond, searchText.trim());
		System.out.println(gradeByPage.toString());
		return gradeByPage;
	}

	/**
	 * 获取所有发布的考试
	 * 
	 * @return
	 */
	@RequestMapping("/selectAllExamName")
	@ResponseBody
	private List<ExamInfo> selectAllExamName() {
		return examService.selectAll();
	}

	/**
	 * 获取答案
	 * 
	 * @param stuNo
	 * @param eId
	 * @return
	 */
	@RequestMapping("/getAnswer")
	@ResponseBody
	private QuestionMarkList getStuAnswer(String id, String stuNo, String eId) {
		System.out.println("查询学号" + stuNo + "--" + eId+"---"+id);
		QuestionMarkList stuAnswer = paperAnswerService.getStuAnswer(stuNo, eId);
		stuAnswer.setStuNo(stuNo);
		stuAnswer.seteId(eId);
		stuAnswer.setId(id);
		System.out.println(stuAnswer.toString());
		return stuAnswer;
	}

	/**
	 * 保存主观题答案
	 * 
	 * @param grade
	 * @return
	 */
	@RequestMapping("/saveSubScore")
	@ResponseBody
	private String saveSubScore(String stuNo, String eId, String id,String subScore, String subAnswer) {
		System.out.println("subAnswer"+subAnswer);
		Grade grade = new Grade();
		grade.setId(Integer.valueOf(id));
		grade.seteId(Integer.valueOf(eId));
		grade.setStuNo(stuNo);
		grade.setSubScore(Integer.valueOf(subScore));
		
		System.out.println(grade);
		boolean updateGrade = gradeService.updateGrade(grade,subAnswer);
		if (updateGrade) {
			return "success";
		}
		return "failure";
	}

	/**
	 * 导出成绩
	 * 
	 * @param eId
	 * @param stuClass
	 */
	@RequestMapping("/export")
	@ResponseBody
	private String exportGrade(String eId, String stuClass,HttpServletRequest request) {
		
		//成绩分析和试卷分析
		List<String> analyzeGrade = gradeService.analyzeGrade(eId, stuClass);
		List<String> analyzePaperQues = paperAnswerService.analyzePaperQues(stuClass, eId);
		
		System.out.println(eId + "==" + stuClass);
		List<StuGrade> stuGrades = gradeService.selectByClassAndEId(eId, stuClass);
		System.out.println(stuGrades.toString());
		if (stuGrades == null || stuGrades.size() == 0) {
			return "error";
		}
		List<String> list = new LinkedList<>();
		list.add("学号");
		list.add("姓名");
		list.add("班级");
		list.add("考试号");
		list.add("客观题分数");
		list.add("主观题分数");
		list.add("总分数");
		HSSFWorkbook expExcel = ExportToExcel.expExcel(list, stuGrades,analyzeGrade,analyzePaperQues);
		//String path = request.getServletContext().getRealPath("/Grades");
		ExportToExcel.outFile(expExcel, "C:/Users/lt/Desktop/exam/成绩导出"+"/"+stuClass+"_"+eId+".xls");
		return stuGrades.size()+"";
	}

	
	/**
	 * 删除部分成绩
	 * 
	 * @param info
	 * @return
	 */
	@RequestMapping("/deleteSome")
	@ResponseBody
	private String deleteSomeGrade(String info){
		System.out.println(info);
		gradeService.deleteGradeAndAnswer(info);
		return "success";
		
	}
}
