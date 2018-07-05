package com.test.util;

public class TestASplit {
	
	public static void main(String[] args) {
		String tString = "1--1";
		String[] split = tString.split("-");
		System.out.println(split.length);
		for (int i = 0; i < split.length; i++) {
			System.out.println(i+":"+split[i]);
		}
	}
}
