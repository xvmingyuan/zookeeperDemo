package com.count.hadoop;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.io.IOUtils;
/**
 * HDFS工具类
 * @author xmy
 * @time：2018年10月25日 下午9:52:37
 */
public class HdfsUtilHA {
	public FileSystem fs = null;
	public static String USERNAME;
	public static String URL;
	public static Configuration CONFIGURATION;

	// 初始化
	public static void initHDFS(String username, String uri, Configuration conf) {
		USERNAME = username;
		URL = uri;
		CONFIGURATION = conf;

	}
	//上传
	public static void uploadingFromLocalFile(String localPath, String inputPath) throws Exception {
		FileSystem fSystem = FileSystem.get(new URI(URL), CONFIGURATION, USERNAME);
		fSystem.copyFromLocalFile(new Path(localPath), new Path(inputPath));
		fSystem.close();
	}
	//创建
	public static void mkdirs(String folder) throws Exception {
		Path path = new Path(folder);
		FileSystem fSystem = FileSystem.get(new URI(URL), CONFIGURATION, USERNAME);
		if (!fSystem.exists(path)) {
			fSystem.mkdirs(path);
			System.out.println("Create: " + folder);
		}
		fSystem.close();
	}
	//删除(递归)
	public static void rm(String path) throws Exception {
		FileSystem fSystem = FileSystem.get(new URI(URL), CONFIGURATION, USERNAME);
		fSystem.delete(new Path(path), true);
		System.out.println("Delete: " + path);
		fSystem.close();
	} 
	//下载
	public static void download(String hdfsPath,String localPath) throws Exception {
		FileSystem fSystem = FileSystem.get(new URI(URL), CONFIGURATION, USERNAME);
		fSystem.copyToLocalFile(new Path(hdfsPath), new Path(localPath));
		fSystem.close();
	}
	//列表
	public static void list() throws Exception{
		FileSystem fSystem = FileSystem.get(new URI(URL), CONFIGURATION, USERNAME);
		RemoteIterator<LocatedFileStatus> files = fSystem.listFiles(new Path("/"), true);
		while (files.hasNext()) {
			LocatedFileStatus file = files.next();
			Path path = file.getPath();
			String name = path.getName();
			System.out.println(name);
		}
		System.out.println("--------------------");
		FileStatus[] listStatus = fSystem.listStatus(new Path("/"));
		for (FileStatus fileStatus : listStatus) {
			String name = fileStatus.getPath().getName();
			System.out.println(name + " " + (fileStatus.isDirectory() ? "dir" : "file"));
		}
		fSystem.close();
	}
	//read
	public static String readStr(String remoteFile) throws Exception{
		Path path = new Path(remoteFile);
		System.out.println("read Result Path: " + remoteFile);
		FileSystem fSystem = FileSystem.get(new URI(URL), CONFIGURATION, USERNAME);
		FSDataInputStream fsdis =null;
		
		OutputStream baos = new ByteArrayOutputStream();
		String str = null;
		
		try {
			if(fSystem.exists(path)) {
				fsdis = fSystem.open(path);
				IOUtils.copyBytes(fsdis, baos, 4096, false);
				str = baos.toString();
			}else {
				str="0";
			}
		} finally {
			IOUtils.closeStream(fsdis);
			fSystem.close();
		}
		System.out.println("read Result value: "+str);
		return str;
	}
}
