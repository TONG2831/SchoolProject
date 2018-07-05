package com.test.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import com.test.dto.StuGrade;

public class ExportToExcel {
	
	public static HSSFWorkbook expExcel(List<String> head, List<StuGrade> body,List<String> grade,List<String> question) {
		//创建一个excel工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建一个sheet工作表
        HSSFSheet sheet = workbook.createSheet("学生信息");
        HSSFSheet sheet1 = workbook.createSheet("成绩分析");
        HSSFSheet sheet2 = workbook.createSheet("试卷分析");
        
        // 创建第0行表头
        HSSFRow row = sheet.createRow(0);
        HSSFRow row1 = sheet1.createRow(0);
        HSSFRow row2 = sheet2.createRow(0);
        HSSFCell cell = null;
        
        // 创建单元格样式,并赋值
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        setBorderStyle(cellStyle, BorderStyle.THIN);
        cellStyle.setFont(setFontStyle(workbook, "黑体", (short) 14));
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        
        //学生成绩表头
        for (int i = 0; i < head.size(); i++) {
            cell = row.createCell(i);
            cell.setCellValue(head.get(i));
            cell.setCellStyle(cellStyle);
        }
        //成绩分析表头
        cell = row1.createCell(0);
        cell.setCellValue("成绩分析");
        cell.setCellStyle(cellStyle);
        
        //试卷分析标题头
        HSSFCell cell2 = row2.createCell(0);
        cell2.setCellValue("试卷分析");
        cell2.setCellStyle(cellStyle);
        
        // 创建样式2,并赋值主体数据
        HSSFCellStyle cellStyle2 = workbook.createCellStyle();
        setBorderStyle(cellStyle2, BorderStyle.THIN);
        cellStyle2.setFont(setFontStyle(workbook, "宋体", (short) 12));
        cellStyle2.setAlignment(HorizontalAlignment.CENTER);
        for (int i = 0, isize = body.size(); i < isize; i++) {
            row = sheet.createRow(i + 1);
            StuGrade stuGrade = body.get(i);
            cell = row.createCell(0);
            cell.setCellValue(stuGrade.getStudent().getStuNo());
            cell.setCellStyle(cellStyle2);
            
            cell = row.createCell(1);
            cell.setCellValue(stuGrade.getStudent().getStuName());
            cell.setCellStyle(cellStyle2);
            
            cell = row.createCell(2);
            cell.setCellValue(stuGrade.getStudent().getStuClass());
            cell.setCellStyle(cellStyle2);
            
            cell = row.createCell(3);
            cell.setCellValue(stuGrade.geteId());
            cell.setCellStyle(cellStyle2);
            
            cell = row.createCell(4);
            cell.setCellValue(stuGrade.getObjScore());
            cell.setCellStyle(cellStyle2);
            
            cell = row.createCell(5);
            cell.setCellValue(stuGrade.getSubScore());
            cell.setCellStyle(cellStyle2);
            
            cell = row.createCell(6);
            cell.setCellValue(stuGrade.getTotalScore());
            cell.setCellStyle(cellStyle2);
        }
        //成绩分析主体数据赋值
        for (int i = 0; i < grade.size(); i++) {
			row1 = sheet1.createRow(i+1);
			cell = row1.createCell(0);
			cell.setCellValue(grade.get(i));
			cell.setCellStyle(cellStyle2);
		}
        //试卷分析主体数据赋值
        for (int i = 0; i < question.size(); i++) {
			row2 = sheet2.createRow(i+1);
			cell2 = row2.createCell(0);
			cell2.setCellValue(question.get(i));
			cell2.setCellStyle(cellStyle2);
		}
        
        sheet1.autoSizeColumn(0);
        sheet2.autoSizeColumn(0);
        for (int i = 0, isize = head.size(); i < isize; i++) {
        	// 自动调整列宽
            sheet.autoSizeColumn(i); 
        }
        
        return workbook;
    }

    /**
     * 保存本地
     * 
     * @param workbook
     * @param path
     */
    public static void outFile(HSSFWorkbook workbook,String path) {
        OutputStream os=null;
        try {
            os = new FileOutputStream(new File(path));
            workbook.write(os);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static HSSFFont setFontStyle(HSSFWorkbook workbook, String name, short height) {
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints(height);
        font.setFontName(name);
        return font;
    }
    private static void setBorderStyle(HSSFCellStyle cellStyle, BorderStyle border) {
        cellStyle.setBorderBottom(border); // 下边框
        cellStyle.setBorderLeft(border);// 左边框
        cellStyle.setBorderTop(border);// 上边框
        cellStyle.setBorderRight(border);// 右边框
    }
}
