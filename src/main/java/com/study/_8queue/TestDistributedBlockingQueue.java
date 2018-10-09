package com.study._8queue;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import com.study._8queue.model.User;


/**
 * 简单分布式队列
 * 
 * @author xmy
 * @time：2018年10月9日 下午8:54:28
 */
public class TestDistributedBlockingQueue {
	public static void main(String[] args) {
		ScheduledExecutorService delayExector = Executors.newScheduledThreadPool(1);
		int delayTime = 5;
		ZkClient zkClient = new ZkClient("it01:2181", 5000, 5000, new SerializableSerializer());
		DistributedBlockingQueue<User> queue = new DistributedBlockingQueue<>(zkClient, "/queue");

		final User user1 = new User();
		user1.setId("1");
		user1.setName("demo1");

		final User user2 = new User();
		user2.setId("2");
		user2.setName("demo2");
		
		try {
			
			delayExector.schedule(new Runnable() {
				
				@Override
				public void run() {
					try {
						queue.push(user1);
						queue.push(user2);
						System.out.println("queue push end!");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, delayTime, TimeUnit.SECONDS);
			
			User u1 = queue.pull();
            User u2 = queue.pull();
            System.out.println("queue.pull() u1 = " + u1.toString());
            System.out.println("queue.pull() u2 = " + u2.toString());
		} catch (Exception e) {
			 e.printStackTrace();
		}finally {
			delayExector.shutdown();
			try {
				delayExector.awaitTermination(2, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
