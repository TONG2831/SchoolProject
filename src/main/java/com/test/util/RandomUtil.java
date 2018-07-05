package com.test.util;

import java.util.LinkedList;
import java.util.List;

import com.test.domain.Question;

/**
 * 随机数工具类
 * 
 * @author lt
 *
 */
public class RandomUtil {

	/**
	 * 随机获取问题集合
	 * 
	 * @param rNum需要的随机数个数
	 * @param random
	 *            随机数范围
	 * @return
	 */
	public static List<Question> getRandomList(Integer rNum, List<Question> random) {
		if (rNum != 0 && rNum <= random.size()) {
			List<Question> list = new LinkedList<>();

			for (int i = 0; i < rNum; i++) {
				int r = (int) (Math.random() * random.size());
				Question q = random.get(r);
				if (list.contains(q)) {
					i--;
				} else {
					list.add(q);
				}
			}
			return list;
		}
		return null;

	}
}
