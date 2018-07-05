package com.test.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.test.dao.PaperMapper;
import com.test.dao.QuestionAndPaperMapper;
import com.test.dao.QuestionMapper;
import com.test.domain.Paper;
import com.test.domain.Question;
import com.test.domain.QuestionInfo;

/**
 * 试题Service层
 * 
 * @author lt
 *
 */
@Service
public class QuestionService {

	private final static int pageSize = 8;

	@Autowired
	QuestionMapper qMapper;

	@Autowired
	PaperMapper pMapper;

	@Autowired
	QuestionAndPaperMapper qPaperMapper;

	/**
	 * 按条件分页查询试题信息
	 * 
	 * @param pageNo
	 * @param qType
	 * @param pName
	 * @param searchText
	 * @return
	 */
	public PageInfo<QuestionInfo> getQuestionInfo(int pageNo, String qType, String pName, String searchText) {
		PageHelper.startPage(pageNo, pageSize);
		List<QuestionInfo> questionInfo = qMapper.selectQInfo(qType, pName, searchText);
		for (QuestionInfo questionInfo2 : questionInfo) {
			System.out.println(questionInfo2);
		}
		return new PageInfo<QuestionInfo>(questionInfo);
	}

	/**
	 * 获取所有试卷信息
	 * 
	 * @return
	 */
	public List<Paper> getAllPaper() {
		return pMapper.selectAll();
	}

	/**
	 * 按id搜索Question
	 * 
	 * @param qId
	 * @return
	 */
	public Question getOneQestion(int qId) {
		return qMapper.selectByPrimaryKey(qId);
	}

	/**
	 * 修改试题
	 * 
	 * @param question
	 */
	public void alterQues(Question question) {
		qMapper.updateByPrimaryKey(question);
	}

	/**
	 * 删除试题,如果在表中存在与试卷的联系,删除
	 * 
	 * @param qId
	 */
	public void deleteQues(Integer qId) {
		// 删除试题库中的试题信息
		qMapper.deleteByPrimaryKey(qId);

		// 删除与试卷之间的联系
		qPaperMapper.deleteByQId(qId);
	}

	/**
	 * 添加试题
	 * 
	 * @param question
	 */
	public void addQues(Question question) {
		question.setqId(null);
		qMapper.insert(question);
		System.out.println("插入后返回的id值:" + question.getqId());
	}

	/**
	 * 获取当前最大主键
	 * 
	 * @return
	 */
	public Integer getQId() {
		return qMapper.getQId();
	}

	/**
	 * 根据试卷id获取所有试题,不分页
	 * 
	 * @param pId
	 * @return
	 */
	public List<QuestionInfo> getQuesByPId(Integer pId) {
		List<QuestionInfo> selectQInfo = qMapper.selectQInfo("all", pId+"", "");
		return selectQInfo;
	}
}
