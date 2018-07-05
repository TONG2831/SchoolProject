package com.test.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.test.domain.PaperInfo;
import com.test.domain.Question;
import com.test.domain.Student;

/**
 * 处理文件 Excel Word
 * 
 * @author lt
 *
 */
public class FileUtils {

	private static Logger logger = Logger.getLogger(FileUtils.class);

	/**
	 * 解析Excel表格,返回List<Student> 集合
	 * 
	 * @param file
	 * @return
	 */
	public static List<Student> analyzeExcel(File file) {

		// 获取文件名
		String fileName = file.getName();

		// 初始化
		Workbook wb = null;
		List<Student> list = new LinkedList<>();

		// 判断文件是否存在
		try {
			if (file.isFile() && file.exists()) {
				String[] split = fileName.split("\\."); // 获取文件格式

				// 根据文件后缀(xls/xlsx)进行判断
				if ("xls".equals(split[1])) {
					FileInputStream fiStream = new FileInputStream(file);
					wb = new HSSFWorkbook(fiStream);
				} else if ("xlsx".equals(split[1])) {
					wb = new XSSFWorkbook(file);
				} else {
					logger.info("文件类型错误!");
				}

				// 解析开始
				Sheet sheet = wb.getSheetAt(0);

				int firstRowIndex = sheet.getFirstRowNum();// 第一行列名
				int lastRowIndex = sheet.getLastRowNum();// 最后一行

				for (int rIndex = firstRowIndex + 1; rIndex <= lastRowIndex; rIndex++) {
					// 获取行
					Row row = sheet.getRow(rIndex);

					// 封装学生信息
					Student student = new Student();

					if (row != null) {
						student.setStuDepartment(row.getCell(0).toString());
						student.setStuClass(row.getCell(1).toString());
						student.setStuNo(row.getCell(2).toString());
						student.setStuName(row.getCell(3).toString());
						student.setStuSex(row.getCell(4).toString());
					}

					list.add(student);
				}

			} else {
				System.out.println("找不到指定文件!");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 解析 Word文件
	 * 
	 * @param file
	 * @throws Exception
	 */
	public static PaperInfo analyzeWord(File file) throws Exception {
		// 判断文件的格式:doc docx
		String fileName = file.getName();
		String[] format = fileName.split("\\.");

		String doc1 = null;

		if ("doc".equals(format[1])) {
			try {
				FileInputStream iStream = new FileInputStream(file);
				HWPFDocument doc = new HWPFDocument(iStream);
				doc1 = doc.getDocumentText();
//				System.out.println("doc1==\r\n" + doc1);
//				StringBuilder doc2 = doc.getText();
//				System.out.println("doc2:==" + doc2);
//				Range rang = doc.getRange();
//				String doc3 = rang.text();
				doc.close();
				iStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("docx".equals(format[1])) {
			try {
				FileInputStream fis = new FileInputStream(file);
				XWPFDocument xdoc = new XWPFDocument(fis);
				XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
				doc1 = extractor.getText();
				extractor.close();
				xdoc.close();
				fis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		logger.info(doc1);
		String[] split = null;
		// 处理换行符
		if (doc1.contains("\n")) {
			split  = doc1.split("\n");
		}else if (doc1.contains("\r")) {
			split = doc1.split("\r");
		}
		String title = null;

		// 获取标题
		if (!split[0].contains("[")) {
			title = split[0].trim();
			System.out.println("标题:" + title);
		}
		
		int singleIndex = -1;
		int douIndex = -1;
		int fIndex = -1;
		int jIndex = -1;
		int anIndex = -1;

		// 循环判断当前试卷中的试题类型
		for (int i = 0; i < split.length; i++) {
			if (split[i].trim().contains("[单选") || split[i].trim().contains("【单选")) {
				singleIndex = i;
			} else if (split[i].trim().contains("[多选") || split[i].trim().contains("【多选")) {
				douIndex = i;
			} else if (split[i].trim().contains("[填空") || split[i].trim().contains("【填空")) {
				fIndex = i;
			} else if (split[i].trim().contains("[判断") || split[i].trim().contains("【判断")) {
				jIndex = i;
			} else if (split[i].trim().contains("[简答") || split[i].trim().contains("【简答")) {
				anIndex = i;
			}
		}

		// 排序
		List<Integer> indexSort = new LinkedList<>();
		indexSort.add(singleIndex);
		indexSort.add(douIndex);
		indexSort.add(fIndex);
		indexSort.add(jIndex);
		indexSort.add(anIndex);
		indexSort.sort(new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				if (o1 >= o2) {
					return 0;
				}
				return -1;
			}

		});

		List<Question> sinChoose = getQuesList(split, indexSort, singleIndex); // 单选
		List<Question> douChoose = getQuesList(split, indexSort, douIndex); // 多选
		List<Question> fixEmpty = getQuesList(split, indexSort, fIndex); // 填空
		List<Question> judge = getQuesList(split, indexSort, jIndex); // 判断
		List<Question> simpleAnswer = getQuesList(split, indexSort, anIndex); // 简答
		
		//所有问题
		List<Question> qList = new LinkedList<>();

		System.out.println("单选");
		if (sinChoose!=null) {
			for (Question question : sinChoose) {
				question.setqType("c");
				System.out.println(question.toString());
			}
			qList.addAll(sinChoose);
		}
		
		System.out.println("多选");
		if (douChoose!=null) {
			for (Question question : douChoose) {
				question.setqType("dc");
				System.out.println(question.toString());
			}
			qList.addAll(douChoose);
		}
		
		System.out.println("判断");
		if (judge!=null) {
			for (Question question : judge) {
				question.setqType("j");
				System.out.println(question.toString());
			}
			qList.addAll(judge);
		}		
		
		System.out.println("填空");
		if (fixEmpty!=null) {
			for (Question question : fixEmpty) {
				question.setqType("f");
				System.out.println(question.toString());
			}
			qList.addAll(fixEmpty);
		}	
		
		System.out.println("简答");
		if (simpleAnswer!=null) {
			for (Question question : simpleAnswer) {
				question.setqType("a");
				System.out.println(question.toString());
			}
			qList.addAll(simpleAnswer);
		}	
		
		PaperInfo pInfo = new PaperInfo();
		pInfo.setpName(title);
		pInfo.setList(qList);
		
		System.out.println("文件删除："+file.delete());
		System.out.println(file.getAbsolutePath());
		return pInfo;
	}


	/**
	 * 获取问题集合
	 * 
	 * @param split
	 * @param indexSort
	 * @param index
	 * @return
	 */
	private static List<Question> getQuesList(String[] split, List<Integer> indexSort, int index) {
		String reg = "^\\d{1,2}([\\.]|[\\、]|[\\s+]).*"; // ^表示字符串开始，\\d表示数字
		int len = 0;
		String content = "";
		String answer = "";
		List<Question> list = new LinkedList<>();
		// 题目存在的情况下,进行下一步操作
		if (index != -1) {
			// 获取该索引在集合中的位置
			int indexOf = indexSort.indexOf(index);
			if (indexOf == indexSort.size() - 1) { // 如果属于最后一个元素
				len = split.length; // 取字符串数组的长度
			} else {
				len = indexSort.get(indexOf + 1) - 1; // 等于下一题目类型所在位置
			}
			// 循环该类型题目区域
			for (int i = index; i < len; i++) {
				if (Pattern.matches(reg, split[i])) { // 题目开头
					int indexOf2 = split[i].indexOf("、")+1;
					content = split[i].trim().substring(indexOf2).trim(); // 内容去除序号
					for (int j = i + 1; j <= len; j++) {
						if (split[j].trim().contains("答案")) {
							// 处理答案的字符串,[答案]长为4,故从第四个截取
							answer = split[j].trim().substring(4);
							Question question = new Question();
							question.setqContent(content);
							question.setqAnswer(answer);
							list.add(question);
							content = "";
							answer = "";
							i = j;
							break;
						} else {
							content = content + " " + split[j];
						}
					}
				}
			}
		} else {
			return null;
		}

		return list;
	}

}
