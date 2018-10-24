package com.paxos.util;

import java.util.Random;

/**
 * 工具类
 *
 */
public class PaxosUtil {
	
	private static final int CHANCE_TO_CRASH = 4;
	private static int m_id=0;
	private static Random random = new Random();
	/*
	 * @brief 提案序列号生成：保证唯一且递增。参考chubby中提议生成算法
	 * @param myID 提议者的ID
	 * @param numCycle 生成提议的轮次
	 * @param proposerCount 提议者个数
	 * @return 生成的提案id
	 */
	public static int generateId(int myID,int numCycle,int proposerCount) {
		int id = numCycle*proposerCount+myID;
		return id;
	}
	
	public static int sleepRandom() {
		int timeInMs = random.nextInt(100)+10;
		try {
			Thread.currentThread().sleep(timeInMs);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return timeInMs;
	}
	
	public static boolean isCrashed() {
		int x = random.nextInt(CHANCE_TO_CRASH);
		if(0==x)
			return true;
		return false;
	}
	public void print(String printStr) {
		System.out.println(printStr);
	}
}
