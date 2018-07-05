package com.test.util;

import java.io.File;

import junit.framework.TestCase;

public class FileUtilsTest extends TestCase {

	public void testAnalyzeWord() throws Exception {
		File file = new File("C:/Users/lt/Desktop/测试.docx");
		FileUtils.analyzeWord(file);
		
		//String string = "ch";
	}

}
