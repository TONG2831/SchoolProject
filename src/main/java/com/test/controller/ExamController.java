package com.test.controller;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.domain.Exam;
import com.test.domain.Grade;
import com.test.domain.PaperAnswer;
import com.test.domain.QuestionInfo;
import com.test.domain.Student;
import com.test.dto.ExamInfo;
import com.test.service.ExamService;
import com.test.service.GradeService;
import com.test.service.PaperAnswerService;
import com.test.service.QuestionService;
import com.test.service.StudentService;

@Controller
@RequestMapping("/doExam")
public class ExamController {

	Logger logger = Logger.getLogger(ExamController.class);

	@Autowired
	ExamService examService;

	@Autowired
	QuestionService questionService;

	@Autowired
	GradeService gradeService;

	@Autowired
	PaperAnswerService paperAnswerService;

	@Autowired
	StudentService studentService;

	/**
	 * 发布考试
	 * 
	 * @param testName
	 * @param paperId
	 * @param selDepartment
	 * @param selClass
	 * @param time
	 */
	@RequestMapping("/createExam")
	@ResponseBody
	public String createExam(Exam exam) {
		logger.info(exam);

		int createExam = examService.createExam(exam);
		if (createExam == 0) {
			return "successs";
		} else if (createExam == 1) {
			return "error";
		} else {
			return "exist";
		}
	}

	/**
	 * 保存考试客观题成绩和考生答案
	 * 
	 * @param objQues
	 * @param subQues
	 * @param objScore
	 * @param request
	 */
	@RequestMapping("/saveResult")
	@ResponseBody
	private void saveResultAndGrade(String objQues, String subQues, Integer objScore, HttpServletRequest request) {
		logger.info(objQues + "/" + subQues + "/" + objScore);

		String eId = (String) request.getSession().getAttribute("eId");
		String stuNo = (String) request.getSession().getAttribute("stuNo");

		PaperAnswer pAnswer = new PaperAnswer();
		pAnswer.seteId(Integer.valueOf(eId));
		pAnswer.setStuNo(stuNo);
		pAnswer.setSubAnswer(subQues);
		pAnswer.setObjAnswer(objQues);

		Grade grade = new Grade();
		grade.seteId(Integer.valueOf(eId));
		grade.setStuNo(stuNo);
		grade.setObjScore(objScore);
		grade.setTotalScore(objScore);

		boolean saveAnswer = paperAnswerService.saveAnswer(pAnswer);
		boolean saveGrade = gradeService.saveGrade(grade);
		if (saveAnswer && saveGrade) {
			logger.info("成绩答案保存成功!");
		} else {
			logger.info("成绩或答案保存失败!");
		}
	}

	/**
	 * 获取所有考试信息
	 * 
	 * @return
	 */
	@RequestMapping("/getAll")
	@ResponseBody
	private List<ExamInfo> getAllExam() {
		return examService.selectAll();
	}

	/**
	 * 保存当前选中的eid 并且验证当前登录的学生是否有考试权限
	 * 
	 * @param eId
	 * @param request
	 */
	@RequestMapping("/saveEId")
	@ResponseBody
	private String saveEId(String eId, String stuNo, HttpServletRequest request) {
		System.out.println("保存Eid:==" + eId);

		// 1.判断考生的考试资格
		// 根据eId获取当前考试的信息，实际是获取学生所在的班级，将班级对比，班级对比成功 ，则准许进入考试。
		ExamInfo exam = examService.getExamInfoById(Integer.valueOf(eId));
		Student student = studentService.getStudent(stuNo);

		// 判断学生是否在考试名单中
		if (!exam.getStuList().contains(student.getStuClass())) {
			return "notexist";
		}

		// 2.首选判断当前学生是否已经参加过考试
		PaperAnswer selectPaperAnswer = paperAnswerService.selectPaperAnswer(stuNo, eId);
		if (selectPaperAnswer != null) {
			return "finished";
		}
		
		// 3.在考试名单中，且没有参加过考试
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(120 * 60); // 设置session的失效时间是两个小时
		session.setAttribute("eId", eId);
		
		return "success";
	}

	/**
	 * 获取当前考试的信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getExamInfo")
	@ResponseBody
	private ExamInfo getExamInfo(HttpServletRequest request) {
		String eId = (String) request.getSession().getAttribute("eId");

		ExamInfo examInfoById = examService.getExamInfoById(Integer.valueOf(eId));

		return examInfoById;

	}

	/**
	 * 拿到当前所有的考试试题
	 * 
	 * 封装之后返回页面
	 * 
	 * @param pId
	 * @return
	 */
	@RequestMapping("/getAllQues")
	@ResponseBody
	private List<String> getExamQues(Integer pId) {
		System.out.println("查询当前试卷的所有试题" + pId);

		List<QuestionInfo> quesByPId = questionService.getQuesByPId(pId);
		List<QuestionInfo> cList = new LinkedList<>();
		List<QuestionInfo> dcList = new LinkedList<>();
		List<QuestionInfo> fList = new LinkedList<>();
		List<QuestionInfo> aList = new LinkedList<>();
		List<QuestionInfo> jList = new LinkedList<>();
		for (QuestionInfo questionInfo : quesByPId) {
			if (questionInfo.getqType().equals("c")) {
				cList.add(questionInfo);
			} else if (questionInfo.getqType().equals("dc")) {
				dcList.add(questionInfo);
			} else if (questionInfo.getqType().equals("f")) {
				fList.add(questionInfo);
			} else if (questionInfo.getqType().equals("a")) {
				aList.add(questionInfo);
			} else if (questionInfo.getqType().equals("j")) {
				jList.add(questionInfo);
			}
		}
		List<String> list = new LinkedList<>();
		int i = 1;

		// 处理单选题
		StringBuffer cdiv = new StringBuffer("<div><span>单选题</span>");
		for (QuestionInfo questionInfo : cList) {
			StringBuffer ul = new StringBuffer();
			String[] split = questionInfo.getqContent().split(" ");
			StringBuffer li = new StringBuffer();
			for (int j = 0; j < split.length; j++) {
				if (split[j] != null) {
					if (j == 0) {
						li.append("<li>" + i + "." + split[j] + "(单选题)</li>");
					} else {
						li.append("<li><input type='radio' name='" + questionInfo.getqId() + "'value='"
								+ split[j].split("、")[0] + "'>" + split[j] + "</li>");
					}
				}
			}
			String answer = "<input type='hidden'name='" + questionInfo.getqId() + "'value='"
					+ questionInfo.getqAnswer() + "' />";
			li.append(answer);
			ul.append("<ul>" + li + "</ul>");
			cdiv.append(ul.toString());
			if (i % 4 == 0) {
				cdiv.append("</div>");
				list.add(cdiv.toString());
				cdiv.delete(0, cdiv.length());
				cdiv.append("<div><span>单选题</span>");
			}
			i++;
		}
		if (!cdiv.toString().equals("<div><span>单选题</span>")) {
			cdiv.append("</div>");
			list.add(cdiv.toString());
		}

		// 处理多选题
		StringBuffer dcdiv = new StringBuffer("<div><span>多选题</span>");
		int i2 = 1;
		for (QuestionInfo questionInfo : dcList) {
			StringBuffer ul = new StringBuffer();
			String[] split = questionInfo.getqContent().split(" ");
			StringBuffer li = new StringBuffer();
			for (int j = 0; j < split.length; j++) {
				if (split[j] != null) {
					if (j == 0) {
						li.append("<li>" + i + "." + split[j] + "(多选题)</li>");
					} else {
						li.append("<li><input type='checkbox' name='" + questionInfo.getqId() + "'value='"
								+ split[j].split("、")[0] + "'>" + split[j] + "</li>");
					}
				}
			}
			String answer = "<input type='hidden'name='" + questionInfo.getqId() + "'value='"
					+ questionInfo.getqAnswer() + "' />";
			li.append(answer);
			ul.append("<ul>" + li + "</ul>");
			dcdiv.append(ul.toString());
			if (i2 % 4 == 0) {
				dcdiv.append("</div>");
				list.add(dcdiv.toString());
				dcdiv.delete(0, dcdiv.length());
				dcdiv.append("<div><span>多选题</span>");
			}
			i2++;
			i++;
		}
		if (!dcdiv.toString().equals("<div><span>多选题</span>")) {
			dcdiv.append("</div>");
			list.add(dcdiv.toString());
		}

		// 处理判断题
		StringBuffer jdiv = new StringBuffer("<div><span>判断题</span>");
		int i3 = 1;
		for (QuestionInfo questionInfo : jList) {
			StringBuffer ul = new StringBuffer();
			StringBuffer li = new StringBuffer();
			li.append("<li>" + i + "." + questionInfo.getqContent() + "(判断题)</li>");
			li.append("<li><input type='radio' name='" + questionInfo.getqId() + "'value='对'>" + "对" + "</li>");
			li.append("<li><input type='radio' name='" + questionInfo.getqId() + "' value='错'>" + "错" + "</li>");
			String answer = "<input type='hidden'name='" + questionInfo.getqId() + "'value='"
					+ questionInfo.getqAnswer() + "' />";
			li.append(answer);
			ul.append("<ul>" + li + "</ul>");
			jdiv.append(ul.toString());
			if (i3 % 4 == 0) {
				jdiv.append("</div>");
				list.add(jdiv.toString());
				jdiv.delete(0, jdiv.length());
				jdiv.append("<div><span>判断题</span>");
			}
			i3++;
			i++;
		}
		if (!jdiv.toString().equals("<div><span>判断题</span>")) {
			jdiv.append("</div>");
			list.add(jdiv.toString());
		}

		// 处理填空题
		StringBuffer fdiv = new StringBuffer("<div><span>填空题</span>");
		int i4 = 1;
		for (QuestionInfo questionInfo : fList) {
			StringBuffer ul = new StringBuffer();
			StringBuffer li = new StringBuffer();
			li.append("<li><label>" + i + "." + questionInfo.getqContent() + "</label><input type='text' name='"
					+ questionInfo.getqId() + "'/>" + "(填空题)</li>");
			String answer = "<input type='hidden'name='" + questionInfo.getqId() + "'value='"
					+ questionInfo.getqAnswer() + "' />";
			li.append(answer);
			ul.append("<ul>" + li + "</ul>");
			fdiv.append(ul);
			if (i4 % 5 == 0) {
				fdiv.append("</>");
				list.add(fdiv.toString());
				fdiv.delete(0, fdiv.length());
				fdiv.append("<div><span>填空题</span>");
			}
			i4++;
			i++;
		}
		if (!fdiv.toString().equals("<div><span>填空题</span>")) {
			fdiv.append("</div>");
			list.add(fdiv.toString());
		}

		// 处理简答题
		StringBuffer adiv = new StringBuffer("<div><span>简答题</span>");
		for (QuestionInfo questionInfo : aList) {
			StringBuffer ul = new StringBuffer();
			StringBuffer li = new StringBuffer();

			li.append("<li><p>" + i + "." + questionInfo.getqContent() + "(简答题)</p></li>");
			li.append("<li><textarea id='content6' name='" + questionInfo.getqId()
					+ "' style='width: 600px; height: 200px; border:1px solid;'></textarea></li>");
			String answer = "<input type='hidden'name='" + questionInfo.getqId() + "'value='"
					+ questionInfo.getqAnswer() + "' />";
			li.append(answer);
			ul.append("<ul>" + li + "</ul>");
			adiv.append(ul);
			adiv.append("</div>");
			list.add(adiv.toString());
			adiv.delete(0, fdiv.length());
			adiv.append("<div><span>简答题</span>");
			i++;
		}

		for (String string : list) {
			System.out.println(string);
		}

		return list;

	}

}
