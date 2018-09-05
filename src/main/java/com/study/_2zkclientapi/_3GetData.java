package com.study._2zkclientapi;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.data.Stat;

import com.study._2zkclientapi.model.User;


public class _3GetData {
	public static void main(String[] args) {
		ZkClient zkClient = new ZkClient("it01:2181", 10000, 10000, new SerializableSerializer());
		System.out.println("OK");
		Stat stat = new Stat();
		User u = zkClient.readData("/node3/node33", stat);
		System.out.println("user:" + u.toString());
		System.out.println(stat);
	}

}
