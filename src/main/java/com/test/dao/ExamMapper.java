package com.test.dao;

import java.util.List;

import com.test.domain.Exam;
import com.test.dto.ExamInfo;

public interface ExamMapper {
	
	List<ExamInfo> selectAll();
	
	Exam selectByEName(String eName);
	
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exam
     *
     * @mbggenerated Sun May 06 11:26:01 CST 2018
     */
    int deleteByPrimaryKey(Integer eId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exam
     *
     * @mbggenerated Sun May 06 11:26:01 CST 2018
     */
    int insert(Exam record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exam
     *
     * @mbggenerated Sun May 06 11:26:01 CST 2018
     */
    int insertSelective(Exam record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exam
     *
     * @mbggenerated Sun May 06 11:26:01 CST 2018
     */
    Exam selectByPrimaryKey(Integer eId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exam
     *
     * @mbggenerated Sun May 06 11:26:01 CST 2018
     */
    int updateByPrimaryKeySelective(Exam record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exam
     *
     * @mbggenerated Sun May 06 11:26:01 CST 2018
     */
    int updateByPrimaryKey(Exam record);
    
}