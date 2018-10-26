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
		profit();
	}
	public static Map<String, String> profit() throws Exception {
		Map<String, String> map = new HashMap<String,String>();
		int sell = getSell();
        int purchase = getPurchase();
        int other = getOther();
        int profit = purchase - sell - other;
        
        System.out.printf("profit = purchase - sell - other = %d - %d - %d = %d\n", purchase , sell, other, profit);
        
        map.put("sell", String.valueOf(sell));
        map.put("purchase", String.valueOf(purchase));
        map.put("other", String.valueOf(other));
        map.put("profit", String.valueOf(profit));
        return map;
	}
	
	public static Integer getPurchase() throws Exception {
		HdfsUtilHA.initHDFS("xmy", Purchase.HDFS, Purchase.getConf());
		return Integer.parseInt(HdfsUtilHA.readStr((Purchase.getPath().get("output") + "/part-r-00000")).trim());
	}
	public static Integer getSell() throws Exception {
		HdfsUtilHA.initHDFS("xmy", Sell.HDFS, Sell.getConf());
		return Integer.parseInt(HdfsUtilHA.readStr((Sell.getPath().get("output") + "/part-r-00000")).trim());
	}
	public static Integer getOther() throws IOException {
		return Other.calcOther(Other.file);
	}
	
}
