package com.test.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.dao.ExamMapper;
import com.test.domain.Exam;
import com.test.dto.ExamInfo;

@Service
public class ExamService {

	@Autowired
	ExamMapper examMapper;

	/**
	 * 创建考试
	 * 
	 * @param exam
	 * @return
	 * 返回 1 ：插入失败
	 * 返回 2 ：考试名称已存在
	 * 返回 0 ：成功
	 */
	public int createExam(Exam exam) {
		int insert = 0;
		if (exam != null) {
			// 查询考试名称是否存在
			Exam selectByEName = examMapper.selectByEName(exam.geteName());
			if (selectByEName == null) {
				insert = examMapper.insertSelective(exam);
			}else{
				return 2;
			}
		}
		if (insert == 1) {
			return 0;
		}
		return 1;
	}

	/**
	 * 查看当前所有考试
	 * 
	 * @return
	 */
	public List<ExamInfo> selectAll() {
		List<ExamInfo> exams = examMapper.selectAll();
		return exams;
	}

	/**
	 * 获取当前考试的所有信息
	 * 
	 * @param eId
	 * @return
	 */
	public ExamInfo getExamInfoById(Integer eId) {
		List<ExamInfo> exams = examMapper.selectAll();
		for (ExamInfo examInfo : exams) {
			if (examInfo.geteId() == eId) {
				return examInfo;
			}
		}
		return null;
	}

}
