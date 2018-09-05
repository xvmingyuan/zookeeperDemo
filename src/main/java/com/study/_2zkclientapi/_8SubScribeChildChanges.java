package com.study._2zkclientapi;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

/**
 * 订阅节点的子节点变化（可以监听不存在的节点当他创建的时候接收到通知）
 */
public class _8SubScribeChildChanges {

	public static void main(String[] args) throws Exception {
		ZkClient zkClient = new ZkClient("it01:2181", 10000, 10000, new SerializableSerializer());
		System.out.println("_8SubScribeChildChanges OK");
		zkClient.subscribeChildChanges("/node3", new ZkChildListener());
		Thread.sleep(Integer.MAX_VALUE);
	}

	private static class ZkChildListener implements IZkChildListener {

		@Override
		public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
			System.out.println(parentPath);
			System.out.println(currentChilds.toString());
		}
	}

}
