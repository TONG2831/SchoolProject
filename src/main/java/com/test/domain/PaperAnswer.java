package com.test.domain;

public class PaperAnswer {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column paper_answer.id
     *
     * @mbggenerated Sat May 12 10:23:37 CST 2018
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column paper_answer.stu_no
     *
     * @mbggenerated Sat May 12 10:23:37 CST 2018
     */
    private String stuNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column paper_answer.e_id
     *
     * @mbggenerated Sat May 12 10:23:37 CST 2018
     */
    private Integer eId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column paper_answer.obj_answer
     *
     * @mbggenerated Sat May 12 10:23:37 CST 2018
     */
    private String objAnswer;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column paper_answer.sub_answer
     *
     * @mbggenerated Sat May 12 10:23:37 CST 2018
     */
    private String subAnswer;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column paper_answer.id
     *
     * @return the value of paper_answer.id
     *
     * @mbggenerated Sat May 12 10:23:37 CST 2018
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column paper_answer.id
     *
     * @param id the value for paper_answer.id
     *
     * @mbggenerated Sat May 12 10:23:37 CST 2018
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column paper_answer.stu_no
     *
     * @return the value of paper_answer.stu_no
     *
     * @mbggenerated Sat May 12 10:23:37 CST 2018
     */
    public String getStuNo() {
        return stuNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column paper_answer.stu_no
     *
     * @param stuNo the value for paper_answer.stu_no
     *
     * @mbggenerated Sat May 12 10:23:37 CST 2018
     */
    public void setStuNo(String stuNo) {
        this.stuNo = stuNo == null ? null : stuNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column paper_answer.e_id
     *
     * @return the value of paper_answer.e_id
     *
     * @mbggenerated Sat May 12 10:23:37 CST 2018
     */
    public Integer geteId() {
        return eId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column paper_answer.e_id
     *
     * @param eId the value for paper_answer.e_id
     *
     * @mbggenerated Sat May 12 10:23:37 CST 2018
     */
    public void seteId(Integer eId) {
        this.eId = eId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column paper_answer.obj_answer
     *
     * @return the value of paper_answer.obj_answer
     *
     * @mbggenerated Sat May 12 10:23:37 CST 2018
     */
    public String getObjAnswer() {
        return objAnswer;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column paper_answer.obj_answer
     *
     * @param objAnswer the value for paper_answer.obj_answer
     *
     * @mbggenerated Sat May 12 10:23:37 CST 2018
     */
    public void setObjAnswer(String objAnswer) {
        this.objAnswer = objAnswer == null ? null : objAnswer.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column paper_answer.sub_answer
     *
     * @return the value of paper_answer.sub_answer
     *
     * @mbggenerated Sat May 12 10:23:37 CST 2018
     */
    public String getSubAnswer() {
        return subAnswer;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column paper_answer.sub_answer
     *
     * @param subAnswer the value for paper_answer.sub_answer
     *
     * @mbggenerated Sat May 12 10:23:37 CST 2018
     */
    public void setSubAnswer(String subAnswer) {
        this.subAnswer = subAnswer == null ? null : subAnswer.trim();
    }

	@Override
	public String toString() {
		return "PaperAnswer [id=" + id + ", stuNo=" + stuNo + ", eId=" + eId + ", objAnswer=" + objAnswer
				+ ", subAnswer=" + subAnswer + "]";
	}
    
}