package com.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.dao.PaperMapper;
import com.test.dao.QuestionAndPaperMapper;
import com.test.dao.QuestionMapper;
import com.test.domain.Paper;
import com.test.domain.PaperInfo;
import com.test.domain.Question;
import com.test.domain.QuestionAndPaper;

@Service
public class FileService {
	@Autowired
	PaperMapper paperMapper;

	@Autowired
	QuestionMapper qMapper;

	@Autowired
	QuestionAndPaperMapper questionAndPaperMapper;

	/**
	 * @param pInfo
	 * @return
	 * 返回1：试卷名已存在
	 * 返回2：创建错误
	 * 返回0：成功
	 */
	public int addPaper(PaperInfo pInfo) {
		// 先添加试卷
		int pId = 0;
		if (pInfo.getpName() != null && !"".equals(pInfo.getpName())) {
			Paper paper = new Paper();
			paper.setpName(pInfo.getpName());
			Paper selectByPName = paperMapper.selectByPName(pInfo.getpName());
			// 判断试卷名称是否存在
			if (selectByPName==null) {	
				paperMapper.insert(paper);
				pId = paper.getpId();
			}else {
				return 1;
			}
		}

		// 遍历试题集合,插入试题,试题-试卷关联
		for (Question question : pInfo.getList()) {
			if (question != null) {
				qMapper.insert(question);
				if (question.getqId() != null && pId != 0) {
					QuestionAndPaper qAndPaper = new QuestionAndPaper();
					qAndPaper.setpId(pId);
					qAndPaper.setqId(question.getqId());
					questionAndPaperMapper.insert(qAndPaper);
					if (qAndPaper.getId() == null) {
						return 2;
					}
				} else {
					return 2;
				}
			}
		}

		return 0;
	}
}
