package com.test.dao;

import com.test.domain.StuLogin;

public interface StuLoginMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stu_login
     *
     * @mbggenerated Mon May 07 13:24:54 CST 2018
     */
    int deleteByPrimaryKey(String stuNo);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stu_login
     *
     * @mbggenerated Mon May 07 13:24:54 CST 2018
     */
    int insert(StuLogin record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stu_login
     *
     * @mbggenerated Mon May 07 13:24:54 CST 2018
     */
    int insertSelective(StuLogin record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stu_login
     *
     * @mbggenerated Mon May 07 13:24:54 CST 2018
     */
    StuLogin selectByPrimaryKey(String stuNo);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stu_login
     *
     * @mbggenerated Mon May 07 13:24:54 CST 2018
     */
    int updateByPrimaryKeySelective(StuLogin record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stu_login
     *
     * @mbggenerated Mon May 07 13:24:54 CST 2018
     */
    int updateByPrimaryKey(StuLogin record);
}