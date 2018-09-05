package com.study._2zkclientapi;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

public class _6DelNode {
	public static void main(String[] args) {
		ZkClient zkClient = new ZkClient("it01:2181", 10000, 10000, new SerializableSerializer());
		System.out.println("OK");
		boolean exists = zkClient.delete("/node3");
		System.out.println("exists:" + exists);
		
		// 循环删除 == rmr /node1
//		boolean exists2 = zkClient.deleteRecursive("/node1");
//		System.out.println("exists:" + exists2);
	}

}
