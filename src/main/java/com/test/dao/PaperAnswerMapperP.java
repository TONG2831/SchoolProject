package com.test.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.test.domain.PaperAnswer;

public interface PaperAnswerMapperP {
   PaperAnswer getStuAnswer(@Param("stuNo")String stuNo, @Param("eId")Integer eId);
   
   List<PaperAnswer> selectStuAnByClass(@Param("stuClass")String stuClass ,@Param("eId")String eId);
}