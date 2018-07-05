package com.test.service;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.test.dao.GradeMapper;
import com.test.dao.PaperAnswerMapper;
import com.test.dao.PaperAnswerMapperP;
import com.test.dao.StudentDao;
import com.test.domain.Grade;
import com.test.domain.PaperAnswer;
import com.test.domain.Student;
import com.test.dto.GradeInfo;
import com.test.dto.StuGrade;

@Service
public class GradeService {

	static private int pageSize = 8;

	@Autowired
	GradeMapper gradeMapper;

	@Autowired
	PaperAnswerService paperAnswerService;

	@Autowired
	PaperAnswerMapper paperAnswerMapper;

	@Autowired
	PaperAnswerMapperP paperAnswerMapperp;

	@Autowired
	StudentDao studentDao;

	public List<String> analyzeGrade(String eId, String stuClass) {

		Map<String, String> map = new HashMap<>();

		// 获取班级
		String[] classes = stuClass.split("/");

		// 获取所有成绩信息
		List<StuGrade> stuGrades = gradeMapper.selectByCond(eId, classes[0], null, null);
			
		// 获取班级所有学生
		List<Student> students = studentDao.selectByClass(classes[0]);
		
		map.put("stuNum", "学生总人数：" + students.size() + "; 已经考试人数：" + stuGrades.size());
		int bad = 0;
		int pass = 0;
		int middel = 0;
		int good = 0;
		int excelent = 0;
		for (StuGrade student : stuGrades) {
			int score = student.getTotalScore();
			if (score < 60) {
				bad++;
			} else if (score >= 60 && score < 70) {
				pass++;
			} else if (score >= 70 && score < 80) {
				middel++;
			} else if (score >= 80 && score < 90) {
				good++;
			} else {
				excelent++;
			}
		}
		
		List<String> list = new LinkedList<>();
		list.add("学生总人数：" + students.size() + "; 已经考试人数：" + stuGrades.size());
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(2);	//精确到小数点后两位
		String result = numberFormat.format((float) bad / (float) stuGrades.size() * 100);
		map.put("bad", "60分以下：" + bad + "人;" + "百分比:" + result + "%");
		list.add("60分以下：" + bad + "人;" + "百分比:" + result + "%");
		
		result =  numberFormat.format((float) pass / (float) stuGrades.size() * 100);
		map.put("pass", "60分以上，70以下：" + pass + "人;" + "百分比:" + result + "%");
		list.add("60分及以上，70以下：" + pass + "人;" + "百分比:" + result + "%");
		
		result =  numberFormat.format((float) middel / (float) stuGrades.size() * 100);
		map.put("middle", "70分以上，80以下：" + middel + "人;" + "百分比:" + result + "%");
		list.add("70分及以上，80以下：" + middel + "人;" + "百分比:" + result + "%");
		
		result =  numberFormat.format((float) good / (float) stuGrades.size() * 100);
		map.put("good", "70分以上，80以下：" + good + "人;" + "百分比:" + result + "%");
		list.add("80分及以上，90以下：" + good + "人;" + "百分比:" + result + "%");
		
		result =  numberFormat.format((float) excelent / (float) stuGrades.size() * 100);
		map.put("excelent", "80分以上，90以下：" + excelent + "人;" + "百分比:" + result + "%");
		list.add("90分到100：" + excelent + "人;" + "百分比:" + result + "%");
		
		System.out.println(map.toString());
		return list;
	}

	/**
	 * 保存成绩
	 * 
	 * @param grade
	 * @return
	 */
	public boolean saveGrade(Grade grade) {
		if (grade != null) {
			int insert = gradeMapper.insertSelective(grade);
			if (insert == 1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 按学号获取所有考试成绩
	 * 
	 * @param stuNo
	 * @return
	 */
	public List<GradeInfo> getGrade(String stuNo) {
		List<GradeInfo> grade = gradeMapper.getGrade(stuNo);
		return grade;
	}

	/**
	 * 分页查询成绩
	 * 
	 * @param eId
	 * @param stuClass
	 * @param pageNo
	 * @param searchCond
	 * @param searchText
	 * @return
	 */
	public PageInfo<StuGrade> getGradeByPage(String eId, String stuClass, String pageNo, String searchCond,
			String searchText) {
		List<StuGrade> grades = gradeMapper.selectByCond(eId, stuClass, searchCond, searchText);
		PageHelper.startPage(Integer.valueOf(pageNo), pageSize);
		return new PageInfo<>(grades);
	}

	/**
	 * 按照班级不分页获取成绩
	 * 
	 * @param stuClass
	 * @return
	 */
	public List<StuGrade> selectByClassAndEId(String eId, String stuClass) {
		return gradeMapper.selectByCond(eId, stuClass, null, null);
	}

	/**
	 * 阅卷后保存主观题成绩,更新总分数, 保存试卷答案
	 * 
	 * @param grade
	 */
	public boolean updateGrade(Grade grade, String subAnswer) {

		// 保存答案
		paperAnswerService.aletrAnswer(grade.getStuNo(), grade.geteId(), subAnswer);

		// 保存成绩
		Grade grade1 = gradeMapper.selectByPrimaryKey(grade.getId());
		grade1.setSubScore(grade.getSubScore());
		grade1.setTotalScore(grade1.getObjScore() + grade1.getSubScore());
		grade1.setIsMarked(1);

		int update = gradeMapper.updateByPrimaryKeySelective(grade1);
		if (update == 1) {
			return true;
		}

		return false;
	}

	public boolean deleteGradeAndAnswer(String info) {
		String[] split = info.split("\\.");
		for (int i = 0; i < split.length; i++) {
			String[] split2 = split[i].split("-");
			String stuNo = split2[0]; // 学号
			Integer eId = Integer.valueOf(split2[1]);// 考试号
			Integer id = Integer.valueOf(split2[2]);// grade的id

			// 1.删除grade
			gradeMapper.deleteByPrimaryKey(id);

			// 2.删除答案
			PaperAnswer stuAnswer = paperAnswerMapperp.getStuAnswer(stuNo, eId);
			paperAnswerMapper.deleteByPrimaryKey(stuAnswer.getId());
		}
		return true;
	}
}
