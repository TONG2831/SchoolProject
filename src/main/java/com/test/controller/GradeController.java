package com.test.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.domain.PaperAnswer;
import com.test.domain.Question;
import com.test.domain.QuestionInfo;
import com.test.dto.CheckMyAnswerQuesInfo;
import com.test.dto.ExamInfo;
import com.test.dto.GradeInfo;
import com.test.service.ExamService;
import com.test.service.GradeService;
import com.test.service.PaperAnswerService;
import com.test.service.QuestionService;

@Controller
@RequestMapping("/doGrade")
public class GradeController {

	@Autowired
	GradeService gradeService;

	@Autowired
	QuestionService questionService;

	@Autowired
	ExamService examService;

	@Autowired
	PaperAnswerService pAnswerService;

	@RequestMapping("/getGrade")
	@ResponseBody
	private List<GradeInfo> getGrade(String stuNo) {
		List<GradeInfo> grade = gradeService.getGrade(stuNo);
		System.out.println(grade);
		return grade;
	}
	
	@RequestMapping("/getMyAnswer")
	@ResponseBody
	private List<CheckMyAnswerQuesInfo> getMyAnswer(String stuNo, String eId) {
		// 1.获取考试信息
		ExamInfo examInfoById = examService.getExamInfoById(Integer.valueOf(eId));

		// 2.获取试题信息
		List<QuestionInfo> questionInfo = questionService.getQuesByPId(examInfoById.getpId());

		PaperAnswer stuAnswer = pAnswerService.selectPaperAnswer(stuNo, eId);

		String objAnswer = stuAnswer.getObjAnswer(); // 学生客观题答案
		String subAnswer = stuAnswer.getSubAnswer(); // 学生主观题答案

		String[] splitObjAnswer = objAnswer.split("_");
		String[] splitSubAnswer = subAnswer.split("_");

		// 3.判断是否是选择题
		// 将题目选择分解封装
		List<CheckMyAnswerQuesInfo> all = new LinkedList<>();	//总
		List<CheckMyAnswerQuesInfo> cList = new LinkedList<>();
		List<CheckMyAnswerQuesInfo> dcList = new LinkedList<>();
		List<CheckMyAnswerQuesInfo> fList = new LinkedList<>();
		List<CheckMyAnswerQuesInfo> aList = new LinkedList<>();
		List<CheckMyAnswerQuesInfo> jList = new LinkedList<>();
		for (QuestionInfo questionInfo1 : questionInfo) {
			if (questionInfo1.getqType().equals("c")) {

				// 初始化对象
				CheckMyAnswerQuesInfo cAnswerQuesInfo = new CheckMyAnswerQuesInfo();

				// 封装问题基本信息
				Question question = new Question();
				question.setqId(questionInfo1.getqId());
				question.setqContent(questionInfo1.getqContent());
				question.setqAnswer(questionInfo1.getqAnswer());
				question.setqType(questionInfo1.getqType());

				// 拆解试题内容，封装试题选项集合
				List<String> list = splitQContent(question);
				cAnswerQuesInfo.setList(list); // 选项
				cAnswerQuesInfo.setQuestion(question); // 问题信息
				cAnswerQuesInfo.setStandAnswer(questionInfo1.getqAnswer()); // 标准答案

				// 获取学生考试答案，并封装
				CheckMyAnswerQuesInfo andPackageStuAnswer = getAndPackageStuAnswer(splitObjAnswer, cAnswerQuesInfo);

				cList.add(andPackageStuAnswer);

			} else if (questionInfo1.getqType().equals("dc")) {
				CheckMyAnswerQuesInfo cAnswerQuesInfo = new CheckMyAnswerQuesInfo();

				// 封装问题基本信息
				Question question = new Question();
				question.setqId(questionInfo1.getqId());
				question.setqContent(questionInfo1.getqContent());
				question.setqAnswer(questionInfo1.getqAnswer());
				question.setqType(questionInfo1.getqType());

				// 拆解试题内容，封装试题选项集合
				List<String> list = splitQContent(question);
				cAnswerQuesInfo.setList(list); // 选项
				cAnswerQuesInfo.setQuestion(question); // 问题信息
				cAnswerQuesInfo.setStandAnswer(questionInfo1.getqAnswer()); // 标准答案

				// 获取学生考试答案，并封装
				CheckMyAnswerQuesInfo andPackageStuAnswer = getAndPackageStuAnswer(splitObjAnswer, cAnswerQuesInfo);

				dcList.add(andPackageStuAnswer);
			} else if (questionInfo1.getqType().equals("f")) {
				CheckMyAnswerQuesInfo cAnswerQuesInfo = new CheckMyAnswerQuesInfo();

				// 封装问题基本信息
				Question question = new Question();
				question.setqId(questionInfo1.getqId());
				question.setqContent(questionInfo1.getqContent());
				question.setqAnswer(questionInfo1.getqAnswer());
				question.setqType(questionInfo1.getqType());

				cAnswerQuesInfo.setQuestion(question); // 问题信息
				cAnswerQuesInfo.setStandAnswer(questionInfo1.getqAnswer()); // 标准答案

				// 获取学生考试答案，并封装
				CheckMyAnswerQuesInfo andPackageStuAnswer = getAndPackageStuAnswer(splitSubAnswer, cAnswerQuesInfo);
				
				fList.add(andPackageStuAnswer);
			} else if (questionInfo1.getqType().equals("a")) {
				CheckMyAnswerQuesInfo cAnswerQuesInfo = new CheckMyAnswerQuesInfo();

				// 封装问题基本信息
				Question question = new Question();
				question.setqId(questionInfo1.getqId());
				question.setqContent(questionInfo1.getqContent());
				question.setqAnswer(questionInfo1.getqAnswer());
				question.setqType(questionInfo1.getqType());

				cAnswerQuesInfo.setQuestion(question); // 问题信息
				cAnswerQuesInfo.setStandAnswer(questionInfo1.getqAnswer()); // 标准答案

				// 获取学生考试答案，并封装
				CheckMyAnswerQuesInfo andPackageStuAnswer = getAndPackageStuAnswer(splitSubAnswer, cAnswerQuesInfo);
				
				aList.add(andPackageStuAnswer);
			} else if (questionInfo1.getqType().equals("j")) {
				CheckMyAnswerQuesInfo cAnswerQuesInfo = new CheckMyAnswerQuesInfo();

				// 封装问题基本信息
				Question question = new Question();
				question.setqId(questionInfo1.getqId());
				question.setqContent(questionInfo1.getqContent());
				question.setqAnswer(questionInfo1.getqAnswer());
				question.setqType(questionInfo1.getqType());

				cAnswerQuesInfo.setQuestion(question); // 问题信息
				cAnswerQuesInfo.setStandAnswer(questionInfo1.getqAnswer()); // 标准答案

				// 获取学生考试答案，并封装
				CheckMyAnswerQuesInfo andPackageStuAnswer = getAndPackageStuAnswer(splitObjAnswer, cAnswerQuesInfo);
				
				jList.add(andPackageStuAnswer);
			}
		}
		all.addAll(cList);
		all.addAll(dcList);
		all.addAll(jList);
		all.addAll(fList);
		all.addAll(aList);
		System.out.println(all);
		return all;

	}

	/**
	 * 解析并封装学生答案
	 * 
	 * @param splitObjAnswer
	 * @param cAnswerQuesInfo
	 * @return
	 */
	private CheckMyAnswerQuesInfo getAndPackageStuAnswer(String[] splitObjAnswer,
			CheckMyAnswerQuesInfo cAnswerQuesInfo) {
		if (splitObjAnswer != null) {	//考生答案为空
			for (int i = 0; i < splitObjAnswer.length; i++) {
				if (splitObjAnswer[i].split("-").length > 1) {
					if (splitObjAnswer[i].split("-")[0].equals(cAnswerQuesInfo.getQuestion().getqId().toString())) {
						if (!"".equals(splitObjAnswer[i].split("-")[1])) {
							cAnswerQuesInfo.setStuAnswer(splitObjAnswer[i].split("-")[1]);
						}
						if (splitObjAnswer[i].split("-").length > 2) {
							cAnswerQuesInfo.setScore(Integer.valueOf(splitObjAnswer[i].split("-")[3]));
						}
					}
				}
			}
			
			if (cAnswerQuesInfo.getStuAnswer()==null) {
				cAnswerQuesInfo.setStuAnswer("未作答");
			}
			String type = cAnswerQuesInfo.getQuestion().getqType();
			// 题目类型为客观题，且题目分数为null
			if ((type.equals("c")||type.equals("dc")||type.equals("j"))&&cAnswerQuesInfo.getScore()==null) {
				cAnswerQuesInfo.setScore(0);
			}
		}else {
			cAnswerQuesInfo.setStuAnswer("未作答");
			cAnswerQuesInfo.setScore(0);
		}
		System.out.println("封装后："+cAnswerQuesInfo);
		return cAnswerQuesInfo;
	}

	/**
	 * 解析试题内容，若试题存在选项，返回选项的list集合
	 * 
	 * @param question
	 * @return
	 */
	private List<String> splitQContent(Question question) {
		String[] splitChoseList = question.getqContent().split(" ");
		List<String> list = new LinkedList<>();
		for (int i = 0; i < splitChoseList.length; i++) {
			list.add(splitChoseList[i]);
		}
		return list;
	}
}
