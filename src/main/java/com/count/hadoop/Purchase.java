package com.count.hadoop;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.print.PrintException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

/**
 * 购买
 * 
 * @author xmy
 * @time：2018年10月25日 下午9:50:34
 */
public class Purchase {
	public static final String HDFS = "hdfs://ns1";
	public static final Pattern DELIMITER = Pattern.compile("[\t,]");
	private static String month = null; // eg: 2013-01

	public static class PurchaseMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

		private Text k = new Text(month);
		private IntWritable v = new IntWritable();
		private int money = 0;

		@Override
		protected void map(LongWritable key, Text values, Context context) throws IOException, InterruptedException {
			System.out.println(values.toString());
			String[] tokens = DELIMITER.split(values.toString());
			if (tokens[3].startsWith(month)) {
				money = Integer.parseInt(tokens[1]) * Integer.parseInt(tokens[2]);// 单价*数量
				v.set(money);
				context.write(k, v);
			}
		}

	}

	public static class PurchaseReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
		private IntWritable v = new IntWritable();
		private int money = 0;

		@Override
		protected void reduce(Text key, Iterable<IntWritable> values, Context context)
				throws IOException, InterruptedException {
			for (IntWritable line : values) {
				money += line.get();
			}
			v.set(money);
			context.write(null, v);
			System.out.println("Output:" + key + "," + money);
		}
	}

	public static int run(Map<String, String> path) throws Exception {

		Configuration conf = getConf();
		// 初始化purchase 将数据加载到HDFS上
		String local_data = path.get("purchase");
		String input = path.get("input");
		String username = path.get("username");
		String output = path.get("output");
		
		if((month = path.get("month"))!=null) {
			// 按月分文件
			output = output+month;
		}else {
			throw new PrintException("need month! ");
		}
		
		HdfsUtilHA.initHDFS(username, HDFS, conf);
		HdfsUtilHA.mkdirs(input);
		HdfsUtilHA.uploadingFromLocalFile(local_data, input);

		// 开始工作
		Job job = Job.getInstance(conf);
		job.setJarByClass(Purchase.class);

		job.setMapperClass(PurchaseMapper.class);
		job.setReducerClass(PurchaseReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		// job.setInputFormatClass(TextInputFormat.class);
		// job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.setInputPaths(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output));

		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static Map<String, String> getPath() {
		Map<String, String> path = new HashMap<String, String>();
		path.put("purchase", "/home/xmy/Desktop/data/purchase.csv");// 本地的数据文件
		path.put("input", HDFS + "/user/bigdata/purchase/input");// HDFS的目录
		path.put("output", HDFS + "/user/bigdata/purchase/output"); // 输出目录
		path.put("username", "xmy"); 
		return path;
	}

	public static Configuration getConf() {
		return new Configuration();
	}

	public static void main(String[] args) throws Exception {
		Map<String, String> path = getPath();
		if(args.length>0) {
			if (args[1] == null) {
				throw new PrintException("need arg[1] : month! ");
			} else {
				path.put("month", args[1]);
				run(path);
			}
		}else {
			System.out.println("need two ages!!!");
		}
		

	}
}
