package com.study._8queue;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import com.demo._8queue.model.User;


/**
 * 测试简单的分布式队列
 * @author xmy
 * @time：2018年10月8日 下午10:51:29
 */
public class TestDistributedSimpleQueue {
	public static void main(String[] args) {
		ZkClient zkClient = new ZkClient("it01:2181",5000,5000,new SerializableSerializer());
		DistributedSimpleQueue<User> queue = new DistributedSimpleQueue<>(zkClient, "/queue");
		User user1 = new User();
        user1.setId("1");
        user1.setName("jerome1");

        User user2 = new User();
        user2.setId("2");
        user2.setName("jerome2");
        try {
			queue.push(user1);
			queue.push(user2);
			System.out.println("queue.push end!");
			
			
			User u1 = queue.pull();
			User u2 = queue.pull();
			
			System.out.println("queue.pull() u1 = " + u1.toString());
            System.out.println("queue.pull() u2 = " + u2.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
