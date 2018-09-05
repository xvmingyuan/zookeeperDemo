package com.study._2zkclientapi;

import java.util.List;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

public class _4GetChild {
	public static void main(String[] args) {
		ZkClient zkClient = new ZkClient("it01:2181", 10000, 10000, new SerializableSerializer());
		System.out.println("OK");
		List<String> children = zkClient.getChildren("/");
		System.out.println("children:" + children);
	}

}
