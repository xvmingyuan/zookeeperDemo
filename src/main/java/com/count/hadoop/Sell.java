package com.count.hadoop;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

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
 * 销售
 * @author xmy
 * @time：2018年10月25日 下午9:50:53
 */

public class Sell {
	
	public static final String HDFS = "hdfs://ns1";
	public static final Pattern DELIMITER = Pattern.compile("[\t,]");
	
	public static class SellMapper extends Mapper<LongWritable, Text, Text, IntWritable>{
		private String month = "2013-01";
        private Text k = new Text(month);
        private IntWritable v = new IntWritable();
        private int money = 0;
		@Override
		protected void map(LongWritable key, Text values, Context context)
				throws IOException, InterruptedException {
			System.out.println(values.toString());
			String[] tokens = DELIMITER.split(values.toString());
			if(tokens[3].startsWith(month)) {
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
        String username = "xmy";
        
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
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		//2
		FileInputFormat.setInputPaths(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output));
		
		
		return job.waitForCompletion(true)?0:1;
	}
	
	public static Map<String, String> getPath(){
		Map<String, String> path = new HashMap<String, String>();
		path.put("sell", "/Users/xmy/Desktop/flow/sell.csv");// 本地的数据文件
		path.put("input", HDFS + "/user/bigdata/sell/input");// HDFS的目录
		path.put("output", HDFS + "/user/bigdata/sell/output"); // 输出目录
		return path;
	}
	
	public static Configuration getConf() {
		return new Configuration();
	}
	
	public static void main(String[] args) throws Exception {
		run(getPath());
	}
}
