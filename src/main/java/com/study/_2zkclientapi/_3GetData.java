package com.study._2zkclientapi;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.data.Stat;

import com.study._2zkclientapi.model.User;
import com.study._6balance.server.ServerData;


public class _3GetData {
//	public static void main(String[] args) {
//		ZkClient zkClient = new ZkClient("it01:2181", 10000, 10000, new SerializableSerializer());
//		System.out.println("OK");
//		Stat stat = new Stat();
//		User u = zkClient.readData("/node3/node33", stat);
//		System.out.println("user:" + u.toString());
//		System.out.println(stat);
//	}
	
	public static void main(String[] args) {
		ZkClient zkClient = new ZkClient("it01:2181", 10000, 10000, new SerializableSerializer());
		System.out.println("查看服务端负载状态");
		Stat stat = new Stat();
		ServerData sd= zkClient.readData("/servers/6001", stat);
		System.out.println("server_1: " + sd.toString());
		ServerData sd1= zkClient.readData("/servers/6000", stat);
		System.out.println("server_2: " + sd1.toString());
	}

}
