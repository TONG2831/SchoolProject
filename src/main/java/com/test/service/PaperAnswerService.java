package com.test.service;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.dao.ExamMapper;
import com.test.dao.PaperAnswerMapper;
import com.test.dao.PaperAnswerMapperP;
import com.test.dao.QuestionAndPaperMapper;
import com.test.dao.QuestionMapper;
import com.test.domain.Exam;
import com.test.domain.PaperAnswer;
import com.test.domain.Question;
import com.test.domain.QuestionAndPaper;
import com.test.dto.QuestionMarkInfo;
import com.test.dto.QuestionMarkList;

@Service
public class PaperAnswerService {

	@Autowired
	PaperAnswerMapper paperAnswerMapper;

	@Autowired
	PaperAnswerMapperP paperAnswerMapperP;

	@Autowired
	ExamMapper examMapper;

	@Autowired
	QuestionMapper questionMapper;
	
	@Autowired
	QuesAndPaperService quesAndPaperService;
	
	@Autowired
	QuestionAndPaperMapper questionAndPaperMapper;
	/**
	 * 分析试卷试题的正确率
	 * @return 
	 */
	public List<String> analyzePaperQues(String stuClass, String eId) {
		
		//根据考试id获取试卷的试题
		Exam exam = examMapper.selectByPrimaryKey(Integer.valueOf(eId));
		// 获取所有试题和试卷的关联
		List<QuestionAndPaper> questionAndPapers = questionAndPaperMapper.selectByPId(exam.getpId());
		
		//获取当场考试的所有学生成绩
		String[] classes = stuClass.split("/");
		List<PaperAnswer> paperAnswers = paperAnswerMapperP.selectStuAnByClass(classes[0],eId);
		
		// 拿到所有的题目序号
		// key:试题编号，value：正确个数
		Map<Integer, Integer> map = new HashMap<>();
		for (QuestionAndPaper questionAndPaper : questionAndPapers) {
			map.put(questionAndPaper.getqId(), 0);
		}
		
		// 循环所有答案
		for (PaperAnswer paperAnswer2 : paperAnswers) {
			String[] split1 = paperAnswer2.getObjAnswer().split("_");
			String[] split2 = paperAnswer2.getSubAnswer().split("_");

			// 循环客观题
			for (int i = 0; i < split1.length; i++) {
				String[] split = split1[i].split("-");
					// 遍历map集合
					for (Integer integer : map.keySet()) {
						// 题号正确,且得分大于0
						if (split[0].equals(integer+"") && Integer.valueOf(split[3]) > 0) {
							map.put(integer, map.get(integer)+1);
						}
					}
			}
			
			// 循环主观题
			for (int i = 0; i < split2.length; i++) {
				String[] split = split2[i].split("-");
				if (split.length==4) {	//==4说明已经阅卷
					// 遍历map集合
					for (Integer integer : map.keySet()) {
						// 题号正确,且得分大于0
						if (split[0].equals(integer+"") && Integer.valueOf(split[3]) > 0) {
							map.put(integer, map.get(integer)+1);
						}
					}
				}
					
			}
		}
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(2);	//精确到小数点后两位
		List<String> list = new LinkedList<>();
		// 计算正确率
		for (Integer integer : map.keySet()) {
			String result = numberFormat.format((float) map.get(integer) / (float) paperAnswers.size() * 100);
			list.add("序号:"+integer+";正确率："+result+"%");
		}
		System.out.println(map.toString());
		return list;
	}

	/**
	 * 考生答案保存
	 * 
	 * @param paperAnswer
	 * @return
	 */
	public boolean saveAnswer(PaperAnswer paperAnswer) {
		if (paperAnswer != null) {
			int insert = paperAnswerMapper.insertSelective(paperAnswer);
			if (insert == 1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 教师阅卷后，修改学生的试卷答案
	 * 
	 * @param stuNo
	 * @param eId
	 * @param subAnswer
	 * @return
	 */
	public boolean aletrAnswer(String stuNo, Integer eId, String subAnswer) {

		PaperAnswer stuAnswer = paperAnswerMapperP.getStuAnswer(stuNo, eId);
		if (stuAnswer != null) {
			stuAnswer.setSubAnswer(subAnswer);
			int updateByPrimaryKey = paperAnswerMapper.updateByPrimaryKey(stuAnswer);
			if (updateByPrimaryKey == 1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取考生答案
	 * 
	 * @param stuNo
	 * @param eId
	 */
	public QuestionMarkList getStuAnswer(String stuNo, String eId) {
		PaperAnswer stuAnswer = paperAnswerMapperP.getStuAnswer(stuNo, Integer.valueOf(eId));
		String sub = stuAnswer.getSubAnswer();

		// 1.分割，获取题目数组

		String[] ques = sub.split("_");
		// 2.根据eId获取各题型分数,
		Exam exam = examMapper.selectByPrimaryKey(Integer.valueOf(eId));

		List<QuestionMarkInfo> listf = new LinkedList<>(); // 填空题
		List<QuestionMarkInfo> lista = new LinkedList<>(); // 简答题

		// 3.分割题目,拿到题目编号,获取题目信息
		for (int i = 0; i < ques.length; i++) {
			QuestionMarkInfo qMInfo = new QuestionMarkInfo();
			String str = ques[i];
			String[] qStr = str.split("-");
			Question question = questionMapper.selectByPrimaryKey(Integer.valueOf(qStr[0]));

			qMInfo.setQuestion(question); // 问题基本信息
			if (qStr.length > 1) {
				qMInfo.setStuAnswer(qStr[1]);// 学生答案
			}
			String qType = question.getqType();

			// 不同的题目类型 题目分值不同
			// 填空题推荐分值计算
			String qAnswer = question.getqAnswer();
			if ("f".equals(qType)) {
				qMInfo.setqScore(exam.getfScore());
				qMInfo.setRecomdScore(0);
				if (qMInfo.getStuAnswer()!=null) {
					if (qAnswer.contains("/")) {
						String[] answers = qAnswer.split("/");
						for (int j = 0; j < answers.length; j++) {
							if (answers[j].equals(qMInfo.getStuAnswer().trim())) {
								qMInfo.setRecomdScore(exam.getfScore());
							}
						}
					}else {
							if (qMInfo.getStuAnswer().trim().equals(qAnswer)) {
								qMInfo.setRecomdScore(exam.getfScore());
							}
					}
				}
				
				
				
				listf.add(qMInfo);
			} else if ("a".equals(qType)) {	// 简答题推荐分值计算
				qMInfo.setRecomdScore(0);
				qMInfo.setqScore(exam.getaScore());
				if (qMInfo.getStuAnswer()!=null) {
					if (qAnswer.contains(" ")) {
						String[] split = qAnswer.split(" ");
						int qScore = 0;
						for (int j = 0; j < split.length; j++) {
							if (qMInfo.getStuAnswer().contains(split[j])) {
								qScore = qScore + qMInfo.getqScore() / split.length;
							}
						}
						qMInfo.setRecomdScore(qScore);
						
					} else {
						if (qMInfo.getStuAnswer().contains(qAnswer)) {
							qMInfo.setRecomdScore(qMInfo.getqScore());
						}
					}
				}
				lista.add(qMInfo);
			}
		}

		// 5.封装成对象,发送至页面
		QuestionMarkList qMarkList = new QuestionMarkList();
		qMarkList.setLista(lista);
		qMarkList.setListf(listf);

		return qMarkList;
	}

	/**
	 * 根据学号和eId考试号查询考生答案
	 * 
	 * @param stuNo
	 * @param eId
	 * @return
	 */
	public PaperAnswer selectPaperAnswer(String stuNo, String eId) {
		return paperAnswerMapperP.getStuAnswer(stuNo, Integer.valueOf(eId));
	}
}
