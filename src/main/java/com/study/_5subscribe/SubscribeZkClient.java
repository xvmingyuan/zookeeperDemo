package com.study._5subscribe;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;

/**
 * 数据的发布和订阅 负责驱动WordServer和ManageServer 主要应用 配置管理、服务发现
 * 
 */
public class SubscribeZkClient {
	/** 客户端数量 */
	private static final int CLIENT_QTY = 5;
	// zookeeper 服务器地址(任意一台zookeeper服务器均可)
	private static final String ZOOKEEPER_SERVER = "it01:2181";
	// 配置config节点路径
	private static final String CONFIG_PATH = "/configs";
	// command 命令发送节点路径
	private static final String COMMAND_PATH = "/commands";
	// servers服务 注册地址路径
	private static final String SERVERS_PATH = "/servers";

	public static void main(String[] args) throws Exception {
		ArrayList<ZkClient> clients = new ArrayList<ZkClient>();
		ArrayList<WorkServer> workServers = new ArrayList<WorkServer>();
		ManagerServer managerServer;
		try {
			ServerConfig serverConfig = new ServerConfig().setDbUrl("jdbc:mysql://localhost:3306/mydb")
					.setDbUser("root").setDbPwd("root");
			// 启动Manage Server
			ZkClient clientManager = new ZkClient(ZOOKEEPER_SERVER, 5000, 5000, new BytesPushThroughSerializer());
			managerServer = new ManagerServer(SERVERS_PATH, COMMAND_PATH, CONFIG_PATH, clientManager, serverConfig);
			managerServer.startServer();

			// 启动Work Server
			for (int i = 0; i < CLIENT_QTY; i++) {
				ZkClient client = new ZkClient(ZOOKEEPER_SERVER, 5000, 5000, new BytesPushThroughSerializer());
				clients.add(client);

				ServerData serverData = new ServerData();
				serverData.setId(i);
				serverData.setName("WorkServer#" + i);
				serverData.setAdress("192.168.1." + i);

				WorkServer workServer = new WorkServer(client, CONFIG_PATH, SERVERS_PATH, serverConfig, serverData);
				workServers.add(workServer);
				workServer.start();
			}

			Thread.sleep(500);
			System.out.println("敲回车键退出！\n");
			new BufferedReader(new InputStreamReader(System.in)).readLine();
		} finally {
			System.out.println("Shutting down...");
			for (WorkServer workServer : workServers) {
				try {
					workServer.stop();
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
