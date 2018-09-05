package com.study._2zkclientapi;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import com.study._2zkclientapi.model.User;


public class _7UpdateData {
	
	public static void main(String[] args) {
		ZkClient zkClient = new ZkClient("it01:2181", 10000, 10000, new SerializableSerializer());
		System.out.println("OK");
		User user = new User().setId(1).setName("node3_update");
		zkClient.writeData("/node3", user);
	}

}
