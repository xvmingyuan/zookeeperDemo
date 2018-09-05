package com.paxos.util;

/**
 *	性能记录
 */
public class PerformanceRecord {
	private final int MAX_ID_VALUE = 200;
	private static volatile  PerformanceRecord record = null;
	private static String[] despArray;
	private static long[] startArray;
	private static int recordCount;
	
	public static PerformanceRecord getInstance() {
		if(null==record) {
			synchronized(PerformanceRecord.class) {
				if(null==record) 
					record = new PerformanceRecord();
			}
		}
		return record;
	}
	
	
	private PerformanceRecord() {
		despArray = new String[MAX_ID_VALUE];
		startArray = new long[MAX_ID_VALUE];
		recordCount = 0;
	}
	public void start(String description,int ID) {
		despArray[ID] = description;
		startArray[ID] = System.currentTimeMillis();
	}
	
	public void end(int ID) {
		long endTime = System.currentTimeMillis();
		String performStr = String.format("%s 耗时为%d ms", despArray[ID],endTime-startArray[ID]);
		print(performStr);
		despArray[ID] = null;
	}
	public void print(String printStr) {
		System.out.println(printStr);
	}
	
}
