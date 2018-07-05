package com.test.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.test.dao.PaperMapper;
import com.test.dao.QuestionAndPaperMapper;
import com.test.dao.QuestionMapper;
import com.test.domain.Paper;
import com.test.domain.Question;
import com.test.domain.QuestionAndPaper;
import com.test.util.RandomUtil;

@Service
public class PaperService {
	Logger logger = Logger.getLogger(PaperService.class);

	static final int pageSize = 6;

	@Autowired
	PaperMapper paperMapper; // 试卷

	@Autowired
	QuestionAndPaperMapper questionAndPaperMapper;// 试题-试卷

	@Autowired
	QuestionMapper questionMapper; // 试题

	/**
	 * 获取分页信息
	 * 
	 * @param pageNum
	 * @return
	 */
	public PageInfo<Paper> getPaper(int pageNum, String searchText) {
		PageHelper.startPage(pageNum, pageSize);
		List<Paper> selectAll = paperMapper.selectPaperByCond(searchText);
		return new PageInfo<>(selectAll);
	}

	/**
	 * 添加试卷 1.添加试卷,返回试卷主键 2.拿到主键后,插入[试题-试卷]表 成功返回true,失败false
	 * 
	 * @param pName
	 * @param qId
	 * @return
	 * 返回 0 : 正确
	 * 返回 1   ： 试卷名称已存在
	 * 返回 2   ： 错误 
	 */
	public int addPaper(String pName, String qId) {
		// 判空
		if (pName == null || "".equals(pName)) {
			logger.info("pName不能为空");
			return 2;
		} else if ("".equals(qId) || qId == null) {
			logger.info("qId不能为空,需要选择试题");
			return 2;
		} else {
			// 插入试卷
			Paper paper = new Paper();
			paper.setpName(pName);
			
			Paper selectByPName = paperMapper.selectByPName(pName);
			int pId = -1;
			if (selectByPName==null) {
				paperMapper.insert(paper);
				pId = paper.getpId();
			}else {
				return 1;
			}

			// 插入[试卷-试题]表
			String[] split = qId.split(" ");
			for (int i = 0; i < split.length; i++) {
				if (!"".equals(split[i]) && split[i] != null) {
					QuestionAndPaper qAndPaper = new QuestionAndPaper();
					qAndPaper.setpId(pId);
					qAndPaper.setqId(Integer.valueOf(split[i]));
					questionAndPaperMapper.insert(qAndPaper);
				}
			}

		}
		return 0;
	}

	/**
	 * 批量删除 pId为所有待删除的PID连接的字符串
	 * 
	 * @param pId
	 */
	public void deleteSome(String pId) {
		System.out.println("待删除的qId:" + pId);
		String[] split = pId.split("\\.");
		for (int i = 0; i < split.length; i++) {
			deletePaper(Integer.valueOf(split[i]));
		}
	}

	/**
	 * 单个删除试卷 删除试卷的同时,级联删除所有试题/试题关联信息
	 * 
	 * @param pId
	 * @return
	 */
	public int deletePaper(int pId) {
		// 查询试题-试卷关联信息
		List<QuestionAndPaper> selectByPId = questionAndPaperMapper.selectByPId(pId);
		for (QuestionAndPaper questionAndPaper : selectByPId) {
			//questionMapper.deleteByPrimaryKey(questionAndPaper.getqId()); // 删除试题
			questionAndPaperMapper.deleteByPrimaryKey(questionAndPaper.getId()); // 删除试题-试卷联系
		}
		return paperMapper.deleteByPrimaryKey(pId);
	}

	/**
	 * 根据试卷Pid,获取paper
	 * 
	 * @param pId
	 * @return
	 */
	public Paper selectPaperByPId(String pId) {
		Paper paper = paperMapper.selectByPrimaryKey(Integer.valueOf(pId));
		return paper;
	}

	/**
	 * 获取所有试卷信息
	 * 
	 * @return
	 */
	public List<Paper> getAllPaper() {
		List<Paper> selectAll = paperMapper.selectAll();
		return selectAll;
	}

	/**
	 * 自动生成试卷
	 * 
	 * @param pName
	 * @param cNum
	 * @param dcNum
	 * @param jNum
	 * @param fNum
	 * @param aNum
	 * @return
	 * 返回1：试卷名称已存在；
	 * 返回2：创建试卷错误
	 * 返回0：成功
	 */
	public int autoCreatePaper(String pName, Integer cNum, Integer dcNum, Integer jNum, Integer fNum,
			Integer aNum) {
		
		// 1.创建试卷,拿试卷id
		Paper paper = new Paper();
		paper.setpName(pName);
		
		// 验证试卷是否存在
		Paper selectByPName = paperMapper.selectByPName(pName);
		int pId  = -1;
		if (selectByPName==null) {
			paperMapper.insert(paper);
			pId = paper.getpId();
		}else {
			return 1;
		}

		// 2.题目分类型存放
		List<Question> questions = questionMapper.selectAll();
		List<Question> cList = new LinkedList<>();
		List<Question> dcList = new LinkedList<>();
		List<Question> jList = new LinkedList<>();
		List<Question> fList = new LinkedList<>();
		List<Question> aList = new LinkedList<>();

		for (Question question : questions) {
			if ("c".equals(question.getqType())) {
				cList.add(question);
			} else if ("dc".equals(question.getqType())) {
				dcList.add(question);
			} else if ("j".equals(question.getqType())) {
				jList.add(question);
			} else if ("f".equals(question.getqType())) {
				fList.add(question);
			} else if ("a".equals(question.getqType())) {
				aList.add(question);
			}
		}
		System.out.println(pName);
		System.out.println(cNum);
		System.out.println(dcNum);
		// 3.根据题目数,随机获取题目
		List<Question> listc = RandomUtil.getRandomList(cNum, cList);
		System.out.println("单选题："+listc);
		List<Question> listdc = RandomUtil.getRandomList(dcNum, dcList);
		List<Question> listj = RandomUtil.getRandomList(jNum, jList);
		List<Question> listf = RandomUtil.getRandomList(fNum, fList);
		List<Question> lista = RandomUtil.getRandomList(aNum, aList);
		
		// 4.将题目和试卷的联系插入数据库
		List<Question> list = new LinkedList<>();
		if (listc!=null) {
			list.addAll(listc);
		}
		if (listdc!=null) {
			list.addAll(listdc);
		}
		if (listj!=null) {
			list.addAll(listj);
		}
		if (listf!=null) {
			list.addAll(listf);
		}
		if (lista!=null) {
			list.addAll(lista);
		}
		for (Question question : list) {
			QuestionAndPaper qAndPaper = new QuestionAndPaper();
			qAndPaper.setpId(pId);
			qAndPaper.setqId(question.getqId());
			questionAndPaperMapper.insert(qAndPaper);
		}
		System.out.println("自动生成试卷："+list);
		
		return 0;
	}

}
