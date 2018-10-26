package com.count.hadoop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
/**
 * 文件操作工具
 * @author xmy
 * @time：2018年7月4日 下午4:36:55
 */
public class FileUtil {
	/**
	 * 打印输出
	 * @param strB_data
	 * @param output
	 */
	public static void outPirintFile(String strB_data, File output) {
		if (output != null) {
			FileOutputStream fs;
			try {
				fs = new FileOutputStream(output, true);
				PrintStream p = new PrintStream(fs);
				p.println(strB_data);
				p.close();
				fs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	/**
	 * 获取文件集合
	 * @param strPath 文件集合路径
	 * @param endsWith 判断文件名是否以 endsWith结尾
	 * @return
	 */
	public static List<File> getFileList(String strPath,String endsWith) {
		List<File> filelist = new LinkedList<File>();
		File dir = new File(strPath);
		File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组

		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				String fileName = files[i].getName();
				if (files[i].isDirectory()) { // 判断是文件还是文件夹
					getFileList(files[i].getAbsolutePath(),endsWith); // 获取文件绝对路径
				} else if (fileName.endsWith(endsWith)) { // 判断文件名是否以.htm结尾
					// String strFileName = files[i].getAbsolutePath();
					// System.out.println("---" + strFileName);
					filelist.add(files[i]);
				} else {
					continue;
				}
			}
		}
		return filelist;
	}
	/**
	 * 文件内容转为字符串
	 * @param file
	 * @return String
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String readFileToString(File file) throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));;
		StringBuffer buffer = new StringBuffer();
		String s = null;
		while ((s = reader.readLine()) != null) {
			buffer.append(s);
		}
		reader.close();
		return buffer.toString();
	}

}
