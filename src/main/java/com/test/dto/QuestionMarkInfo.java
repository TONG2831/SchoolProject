package com.test.dto;

import com.test.domain.Question;

/**
 * 阅卷时,用来存储试题信息
 * 
 * @author lt
 *
 */
public class QuestionMarkInfo {
	private Question question;

	private String stuAnswer;

	private Integer qScore;

	private Integer recomdScore;

	
	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public String getStuAnswer() {
		return stuAnswer;
	}

	public void setStuAnswer(String stuAnswer) {
		this.stuAnswer = stuAnswer;
	}

	public Integer getqScore() {
		return qScore;
	}

	public void setqScore(Integer qScore) {
		this.qScore = qScore;
	}

	public Integer getRecomdScore() {
		return recomdScore;
	}

	public void setRecomdScore(Integer recomdScore) {
		this.recomdScore = recomdScore;
	}

	@Override
	public String toString() {
		return "QuestionMarkInfo [question=" + question + ", stuAnswer=" + stuAnswer + ", qScore=" + qScore
				+ ", recomdScore=" + recomdScore + "]";
	}

}
