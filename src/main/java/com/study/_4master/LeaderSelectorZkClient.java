package com.study._4master;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

/**
 * master选举调度器，用来启动和停止Worker Server
 * 模拟Zookeeper选举过程 
 * 
 * @author xmy
 * @time：2018年9月9日 下午3:17:54
 */
public class LeaderSelectorZkClient {
	/** 启动的服务个数 */
	private static final int CLIENT_QTY = 10;

	private static final String ZOOKEEPER_SERVER = "it01:2181";

	public static void main(String[] args) throws Exception {
		// 保存所有zkClient的列表
		ArrayList<ZkClient> clients = new ArrayList<ZkClient>();
		// 保存所有服务器的列表
		ArrayList<WorkServer> workServers = new ArrayList<WorkServer>();

		try {
			for (int i = 0; i < CLIENT_QTY; i++) {
				// 创建zkClient
				ZkClient zkClient = new ZkClient(ZOOKEEPER_SERVER, 1000, 1000, new SerializableSerializer());
				clients.add(zkClient);

				// 创建serverData
				RunningData runningData = new RunningData();
				runningData.setCid(Long.valueOf(i));
				runningData.setName("Zookeeper 服务器#" + i);

				// 创建服务
				WorkServer workServer = new WorkServer(runningData);
				workServer.setZkClient(zkClient);
				workServers.add(workServer);

				workServer.startServer();

			}
			System.out.println("敲回车键退出！\n");
			new BufferedReader(new InputStreamReader(System.in)).readLine();
		} finally {
			System.out.println("Shutting down...");
			for (WorkServer workServer : workServers) {
				try {
					workServer.stopServer();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (ZkClient client : clients) {
				try {
					client.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

	}
}
