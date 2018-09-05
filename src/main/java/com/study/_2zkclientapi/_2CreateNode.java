package com.study._2zkclientapi;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;

import com.demo._2zkclientapi.model.User;

public class _2CreateNode {
	public static void main(String[] args) {
		ZkClient zkClient = new ZkClient("it01:2181", 10000, 10000, new SerializableSerializer());
		System.out.println("OK");
		
		User user = new User().setId(2).setName("node33");
		String path = zkClient.create("/node3/node33", user, CreateMode.PERSISTENT);
		System.out.println("created path:" + path);
	}

}
