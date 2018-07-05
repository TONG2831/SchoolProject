package com.test.domain;

import java.util.List;

/**
 * 用于解析word试卷时,获取试卷名和问题集合
 * 
 * @author lt
 *
 */
public class PaperInfo {
	private String pName;
	private List<Question> list;
	public String getpName() {
		return pName;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}
	public List<Question> getList() {
		return list;
	}
	public void setList(List<Question> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return "PaperInfo [pName=" + pName + ", list=" + list + "]";
	}
}
