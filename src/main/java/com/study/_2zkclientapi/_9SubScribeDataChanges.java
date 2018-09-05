package com.study._2zkclientapi;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;

/**
 * 订阅节点的内容的变化
 *
 */
public class _9SubScribeDataChanges {

	public static void main(String[] args) throws Exception {
		ZkClient zkClient = new ZkClient("it01:2181", 10000, 10000, new BytesPushThroughSerializer());
		System.out.println("_9SubScribeDataChanges OK");
		zkClient.subscribeDataChanges("/node3", new ZkDataListener());
		Thread.sleep(Integer.MAX_VALUE);
	}

	private static class ZkDataListener implements IZkDataListener {

		@Override
		public void handleDataChange(String dataPath, Object data) throws Exception {
			System.out.println(dataPath + ":" + data.toString());
			
		}

		@Override
		public void handleDataDeleted(String dataPath) throws Exception {
			System.out.println(dataPath);
			
		}

	
	}

}
