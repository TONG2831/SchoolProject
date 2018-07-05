package com.test.domain;

/**
 * 学生表\ 已经重写hashcode和equals方法
 * 
 * @author lt
 *
 */
public class Student {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((stuClass == null) ? 0 : stuClass.hashCode());
		result = prime * result + ((stuDepartment == null) ? 0 : stuDepartment.hashCode());
		result = prime * result + ((stuName == null) ? 0 : stuName.hashCode());
		result = prime * result + ((stuNo == null) ? 0 : stuNo.hashCode());
		result = prime * result + ((stuSex == null) ? 0 : stuSex.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		if (stuNo == null) {
			if (other.stuNo != null) {
				return false;
			}
		} else if (stuNo.equals(other.stuNo)) {
			return true;
		}
		
		return false;
	}

	private String stuNo; // 学号
	private String stuName; // 姓名
	private String stuSex; // 性别
	private String stuClass; // 班级
	private String stuDepartment; // 院系

	public Student() {
		super();
	}

	public Student(String stuNo, String stuName, String stuSex, String stuClass, String stuDepartment) {
		super();
		this.stuNo = stuNo;
		this.stuName = stuName;
		this.stuSex = stuSex;
		this.stuClass = stuClass;
		this.stuDepartment = stuDepartment;
	}

	public String getStuNo() {
		return stuNo;
	}

	public String getStuSex() {
		return stuSex;
	}

	public void setStuSex(String stuSex) {
		this.stuSex = stuSex;
	}

	public void setStuNo(String stuNo) {
		this.stuNo = stuNo;
	}

	public String getStuName() {
		return stuName;
	}

	public void setStuName(String stuName) {
		this.stuName = stuName;
	}

	public String getStuClass() {
		return stuClass;
	}

	public void setStuClass(String stuClass) {
		this.stuClass = stuClass;
	}

	public String getStuDepartment() {
		return stuDepartment;
	}

	public void setStuDepartment(String stuDepartment) {
		this.stuDepartment = stuDepartment;
	}

	@Override
	public String toString() {
		return "Student [stuNo=" + stuNo + ", stuName=" + stuName + ", stuSex=" + stuSex + ", stuClass=" + stuClass
				+ ", stuDepartment=" + stuDepartment + "]";
	}

}
