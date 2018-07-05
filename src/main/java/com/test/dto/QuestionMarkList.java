package com.test.dto;

import java.util.List;

/**
 * 两个对象 1.封装填空题列表 2.封装问答题列表
 * 
 * 返回页面
 * 
 * @author lt
 *
 */
public class QuestionMarkList {

	private String stuNo;
	
	private String eId;
	
	private String id;//成绩表id
	

	private List<QuestionMarkInfo> listf;	//填空题

	private List<QuestionMarkInfo> lista;	//简答题

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getStuNo() {
		return stuNo;
	}

	public void setStuNo(String stuNo) {
		this.stuNo = stuNo;
	}

	public String geteId() {
		return eId;
	}

	public void seteId(String eId) {
		this.eId = eId;
	}

	public List<QuestionMarkInfo> getListf() {
		return listf;
	}

	public void setListf(List<QuestionMarkInfo> listf) {
		this.listf = listf;
	}

	public List<QuestionMarkInfo> getLista() {
		return lista;
	}

	public void setLista(List<QuestionMarkInfo> lista) {
		this.lista = lista;
	}

	@Override
	public String toString() {
		return "QuestionMarkList [stuNo=" + stuNo + ", eId=" + eId + ", id=" + id + ", listf=" + listf + ", lista="
				+ lista + "]";
	}
}
