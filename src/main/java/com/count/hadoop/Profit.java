package com.count.hadoop;

import java.io.IOException;
/**
 * 利润
 * @author xmy
 * @time：2018年10月25日 下午9:50:08
 */
public class Profit {
	
	public static void main(String[] args) throws Exception {
		profit();
	}
	public static String profit() throws Exception {
		int sell = getSell();
        int purchase = getPurchase();
        int other = getOther();
        int profit = sell - purchase - other;
        System.out.printf("profit = sell - purchase - other = %d - %d - %d = %d\n", sell, purchase, other, profit);
        return "profit = sell - purchase - other = "+profit;
	}
	
	public static int getPurchase() throws Exception {
		HdfsUtilHA.initHDFS("xmy", Purchase.HDFS, Purchase.getConf());
		return Integer.parseInt(HdfsUtilHA.readStr((Purchase.getPath().get("output") + "/part-r-00000").trim()));
	}
	public static int getSell() throws Exception {
		HdfsUtilHA.initHDFS("xmy", Sell.HDFS, Sell.getConf());
		return Integer.parseInt(HdfsUtilHA.readStr((Sell.getPath().get("output") + "/part-r-00000").trim()));
	}
	public static int getOther() throws IOException {
		return Other.calcOther(Other.file);
	}
	
}
