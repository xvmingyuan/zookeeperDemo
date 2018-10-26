package com.count.hadoop;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/**
 * 利润
 * @author xmy
 * @time：2018年10月25日 下午9:50:08
 */
public class Profit {
	
	public static void main(String[] args) throws Exception {
		profit(args);
	}
	public static Map<String, String> profit(String[] args) throws Exception {
		Map<String, String> map = new HashMap<String,String>();
		int sell = getSell(args[0]);
        int purchase = getPurchase(args[0]);
        int other = getOther(args[0]);
        int profit = purchase - sell - other;
        
        System.out.printf("profit = purchase - sell - other = %d - %d - %d = %d\n", purchase , sell, other, profit);
        
        map.put("sell", String.valueOf(sell));
        map.put("purchase", String.valueOf(purchase));
        map.put("other", String.valueOf(other));
        map.put("profit", String.valueOf(profit));
        return map;
	}
	
	public static Integer getPurchase(String time) throws Exception {
		HdfsUtilHA.initHDFS("xmy", Purchase.HDFS, Purchase.getConf());
		return Integer.parseInt(HdfsUtilHA.readStr((Purchase.getPath().get("output") +time+ "/part-r-00000")).trim());
	}
	public static Integer getSell(String time) throws Exception {
		HdfsUtilHA.initHDFS("xmy", Sell.HDFS, Sell.getConf());
		return Integer.parseInt(HdfsUtilHA.readStr((Sell.getPath().get("output") +time+ "/part-r-00000")).trim());
	}
	public static Integer getOther(String time) throws IOException {
		return Other.calcOther(Other.file,time);
	}
	
}
