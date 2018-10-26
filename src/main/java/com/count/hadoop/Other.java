package com.count.hadoop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;
/**
 * 其他
 * @author xmy
 * @time：2018年10月25日 下午9:51:16
 */
public class Other {
//	public static String file = "/Users/xmy/Desktop/flow/other.csv";
	public static String file = "/home/xmy/Desktop/data/other.csv";
	public static final Pattern DELIMITER = Pattern.compile("[/t,]");

	public static int calcOther(String file,String time) throws IOException {
		int money = 0;
		BufferedReader br = new BufferedReader(new FileReader(new File(file)));
		String s = null;

		while ((s = br.readLine()) != null) {
			String[] tokens = DELIMITER.split(s);
			if (tokens[0].startsWith(time)) {
				money +=Integer.parseInt(tokens[1]);
			}
		}
		br.close();
		System.out.println("Other Output:" + time + "," + money);
		return money;
	}
	public static void main(String[] args) throws IOException {
		calcOther(file,args[0]);
	}
}
