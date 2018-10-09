package com.study._6balance.server;

import java.util.ArrayList;

/**
 * 调度类
 * 
 * @author xmy
 * @time：2018年9月23日 下午5:04:16
 */
public class ServerRunner {
	// 服务器数量
	private static final int SERVER_QTY = 2;
	/** Zookeeper服务器地址 */
	private static final String ZOOKEEPER_SERVER = "it01:2181";
	/** 服务注册节点 */
	private static final String SERVERS_PATH = "/servers";
	public static void main(String[] args) {
		ArrayList<Thread> threadList = new ArrayList<Thread>();
		for (int i=0; i<SERVER_QTY;i++) {
			final Integer count = i;
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					ServerData serverData = new ServerData();
					serverData.setBalance(0);
					serverData.setHost("127.0.0.1");
					serverData.setPort(6000+count);
					
					ServerImpl server = new ServerImpl(ZOOKEEPER_SERVER, SERVERS_PATH, serverData);
					server.bind();
					
				}
			});
			threadList.add(thread);
			thread.start();
		}
		
		for(int i=0;i<threadList.size();i++) {
			try {
				// 等待该线程终止
				threadList.get(i).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}

}
