package com.test.domain;

/**
 * 试题信息dto
 * 
 * @author lt
 *
 */
public class QuestionInfo {
	private Integer qId;	//试题id

	private String qContent;	//试题内容

	private String qAnswer;	//试题答案

	private String qType;	//试题类型
	
	private Integer pId;
	private String pName;

	public Integer getpId() {
		return pId;
	}


	public void setpId(Integer pId) {
		this.pId = pId;
	}


	public String getpName() {
		return pName;
	}


	public void setpName(String pName) {
		this.pName = pName;
	}


	public QuestionInfo() {}


	public Integer getqId() {
		return qId;
	}

	public void setqId(Integer qId) {
		this.qId = qId;
	}

	public String getqContent() {
		return qContent;
	}

	public void setqContent(String qContent) {
		this.qContent = qContent;
	}

	public String getqAnswer() {
		return qAnswer;
	}

	public void setqAnswer(String qAnswer) {
		this.qAnswer = qAnswer;
	}

	public String getqType() {
		return qType;
	}

	public void setqType(String qType) {
		this.qType = qType;
	}

	@Override
	public String toString() {
		return "QuestionInfo [qId=" + qId + ", qContent=" + qContent + ", qAnswer=" + qAnswer + ", qType=" + qType
				+ ", pId=" + pId + ", pName=" + pName + "]";
	}

	
}
