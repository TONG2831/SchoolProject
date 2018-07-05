package com.test.dto;

import java.util.List;

import com.test.domain.Question;

/**
 * 查看我的考试答案封装类
 * 
 * @author lt
 *
 */
public class CheckMyAnswerQuesInfo {
	
	private Question question;	//试题信息
	
	private Integer score;	//题目得分
	
	private String stuAnswer;	//学生答案
	
	private String standAnswer;	//标准答案
	
	private List<String> list;	//选项列表

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getStuAnswer() {
		return stuAnswer;
	}

	public void setStuAnswer(String stuAnswer) {
		this.stuAnswer = stuAnswer;
	}

	public String getStandAnswer() {
		return standAnswer;
	}

	public void setStandAnswer(String standAnswer) {
		this.standAnswer = standAnswer;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "CheckMyAnswerQuesInfo [question=" + question + ", score=" + score + ", stuAnswer=" + stuAnswer
				+ ", standAnswer=" + standAnswer + ", list=" + list + "]";
	}
	
}
