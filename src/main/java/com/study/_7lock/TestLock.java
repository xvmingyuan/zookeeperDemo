package com.study._7lock;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;

/**
 * 测试分布式锁
 * 
 * @author xmy
 * @time：2018年9月24日 下午6:00:43
 */
public class TestLock {
	public static String zkIP = "it01:2181";
	public static String path = "/locker";

	public static void main(String[] args) {
		// 需要手动创建节点 /locker
		ZkClient zkClient = new ZkClient(zkIP, 5000, 5000, new BytesPushThroughSerializer());
		LockIImpl lock1 = new LockIImpl(zkClient, path);
		
		ZkClient zkClient2 = new ZkClient(zkIP, 5000, 5000, new BytesPushThroughSerializer());
		LockIImpl lock2 = new LockIImpl(zkClient2, path);
		try {
			lock1.getLock();
			System.out.println("Client1 is get lock!");
			Thread client2Thd = new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						lock2.getLock();
						System.out.println("Client2 is get lock");
						lock2.releaseLock();
						System.out.println("Client2 is released lock");
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			});
			client2Thd.start();
			
			// 5s 后lock1释放锁
            Thread.sleep(5000);
            lock1.releaseLock();
            System.out.println("Client1 is released lock");
            
            client2Thd.join();
            
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
