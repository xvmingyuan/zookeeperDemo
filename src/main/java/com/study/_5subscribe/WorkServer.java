package com.study._5subscribe;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;

import com.alibaba.fastjson.JSON;

/**
 * 
 * @author xmy
 * @time：2018年9月13日 下午10:19:23
 */
public class WorkServer {
	private ZkClient zkClient;
	private String configPath;
	private String serversPath;
	private ServerConfig serverConfig;
	private ServerData serverData;
	private IZkDataListener dataListener;

	public WorkServer(ZkClient zkClient, String configPath, String serversPath, ServerConfig serverConfig,
			ServerData serverData) {
		this.zkClient = zkClient;
		this.configPath = configPath;
		this.serversPath = serversPath;
		this.serverConfig = serverConfig;
		this.serverData = serverData;
		this.dataListener = new MyZkDataListener();
	}

	public void start() {
		System.out.println("start work server.");
		registMe();
		zkClient.subscribeDataChanges(configPath, dataListener);

	}

	public void stop() {
		System.out.println("stop work server.");
		zkClient.unsubscribeDataChanges(serversPath, dataListener);

	}

	/**
	 * 注册自己的信息到Server节点(服务发现,发布)
	 * 
	 */
	private void registMe() {
		System.out.println("work server regist to /server.");
		String mepath = serversPath.concat("/").concat(serverData.getAdress());
		try {
			// 创建临时节点
			zkClient.createEphemeral(mepath, JSON.toJSONString(serverData).getBytes());
		} catch (ZkNoNodeException e) {
			// 没有父节点 创建永久父节点
			zkClient.createPersistent(serversPath, true);
			registMe();
		}
	}

	private void updateConfig(ServerConfig serverConfig) {
		this.serverConfig = serverConfig;
	}

	private class MyZkDataListener implements IZkDataListener {
		/**
		 * 数据有变化(配置管理,订阅)
		 */
		@Override
		public void handleDataChange(String dataPath, Object data) throws Exception {
			String retJson = new String((byte[]) data);
			ServerConfig serverConfigLocal = JSON.parseObject(retJson, ServerConfig.class);
			updateConfig(serverConfigLocal);
			System.out.println("Work server : new Work server config is = " + serverConfig.toString());
		}

		@Override
		public void handleDataDeleted(String dataPath) throws Exception {
		}
	}

}
