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
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
/**
 * 销售
 * @author xmy
 * @time：2018年10月25日 下午9:50:53
 */

public class Sell {
	
	public static final String HDFS = "hdfs://ns1";
	public static final Pattern DELIMITER = Pattern.compile("[\t,]");
	public static String month;
	public static class SellMapper extends Mapper<LongWritable, Text, Text, IntWritable>{
		
		public Text k = new Text();
        private IntWritable v = new IntWritable();
        private int money = 0;
		@Override
		protected void map(LongWritable key, Text values, Context context)
				throws IOException, InterruptedException {
			//System.out.println(values.toString());
			String[] tokens = DELIMITER.split(values.toString());
			k.set(context.getConfiguration().get("month"));
			if(tokens[3].startsWith(context.getConfiguration().get("month"))) {
				money = Integer.parseInt(tokens[1]) * Integer.parseInt(tokens[2]);//单价*数量
				v.set(money);
				context.write(k, v);
			}
			
		}
	}
	
	public static  class SellReducer extends Reducer<Text, IntWritable, Text, IntWritable>{
		private IntWritable v = new IntWritable();
        private int money = 0;
		@Override
		protected void reduce(Text key, Iterable<IntWritable> valuse,Context context) throws IOException, InterruptedException {
			for (IntWritable line : valuse) {
				money+=line.get();
			}
			v.set(money);
			context.write(null, v);
			System.out.println("Output:" + key + "," + money);
		}
	}
	
	public static int run(Map<String,String> path) throws Exception{
		Configuration conf = getConf();
		String local_data = path.get("sell");
        String input = path.get("input");
        String output = path.get("output");
        String username = path.get("username");
        if((month = path.get("month"))!=null) {
        		conf.set("month", month);
			// 按月分文件
			output = output+month;
		}else {
			throw new PrintException("need month! ");
		}
		//初始化HDFS
        HdfsUtilHA.initHDFS(username, HDFS, conf);
		HdfsUtilHA.mkdirs(input);
		HdfsUtilHA.uploadingFromLocalFile(local_data, input);
		
		Job job = Job.getInstance(conf);
		//1
		job.setJarByClass(Sell.class);
		//2
		job.setMapperClass(SellMapper.class);
		job.setReducerClass(SellReducer.class);
		//2
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		//2
//		job.setInputFormatClass(TextInputFormat.class);
//		job.setOutputFormatClass(TextOutputFormat.class);
		
		//2
		FileInputFormat.setInputPaths(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output));
		
		
		return job.waitForCompletion(true)?0:1;
	}
	
	public static Map<String, String> getPath(){
		Map<String, String> path = new HashMap<String, String>();
		path.put("sell", "/home/xmy/Desktop/data/sell.csv");// 本地的数据文件
		path.put("input", HDFS + "/user/bigdata/sell/input");// HDFS的目录
		path.put("output", HDFS + "/user/bigdata/sell/output"); // 输出目录
		path.put("username", "xmy"); // 输出目录
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
