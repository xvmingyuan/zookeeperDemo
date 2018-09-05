package com.study._2zkclientapi;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

public class _5NodeExists {
	public static void main(String[] args) {
		ZkClient zkClient = new ZkClient("it01:2181", 10000, 10000, new SerializableSerializer());
		System.out.println("OK");
		boolean exists = zkClient.exists("/node1");
		System.out.println("exists:" + exists);
	}

}
