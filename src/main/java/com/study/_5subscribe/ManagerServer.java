package com.study._5subscribe;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;

import com.alibaba.fastjson.JSON;

public class ManagerServer {
	private String serverPath;
	private String commandPath;
	private String configPath;
	private ZkClient zkClient;
	private ServerConfig serverConfig;
	private IZkDataListener dataListener;
	private IZkChildListener childListener;
	private List<String> workServerList;

	public ManagerServer(String serverPath, String commandPath, String configPath, ZkClient zkClient,
			ServerConfig serverConfig) {
		this.serverPath = serverPath;
		this.commandPath = commandPath;
		this.configPath = configPath;
		this.zkClient = zkClient;
		this.serverConfig = serverConfig;
		this.dataListener = new MyDataListener();
		this.childListener = new MyChildListener();

	}

	/*
	 * 执行命令 1: list 2: create 3: modify
	 */
	public void exeCmd(String cmdType) {
		if ("list".equals(cmdType)) {
			exeList();
		} else if ("create".equals(cmdType)) {
			exeCreate();
		} else if ("modify".equals(cmdType)) {
			exeModify();
		} else {
			System.out.println("mange server ：error command! cmdType = " + cmdType);
		}
	}

	public void exeList() {
		System.out.println(workServerList.toString()+"\n");
	}

	public void exeCreate() {
		if(!zkClient.exists(configPath)) {
			try {
				zkClient.createPersistent(configPath,JSON.toJSONString(serverConfig).getBytes());
			} catch (ZkNodeExistsException e) {
				zkClient.writeData(configPath, JSON.toJSONString(serverConfig).getBytes());
			} catch (ZkNoNodeException e) {
				String parentDir = configPath.substring(0, configPath.lastIndexOf("/"));
				zkClient.createPersistent(parentDir,true);
				exeCreate();
				
			}
		}else {
			System.out.println("manage server : " + configPath + " is exist.");
		}
		
	}

	public void exeModify() {
		serverConfig.setDbUser(serverConfig.getDbUser()+"_modify");
		try {
			zkClient.writeData(configPath, JSON.toJSONString(serverConfig).getBytes());
		} catch (ZkNoNodeException e) {
			exeCreate();
		}
	}

	public void startServer() {
		System.out.println("start manage server.");
		zkClient.subscribeChildChanges(serverPath, childListener);
		zkClient.subscribeDataChanges(commandPath, dataListener);
	}

	public void stopServer() {
		System.out.println("stop manage server.");
		zkClient.unsubscribeChildChanges(serverPath, childListener);
		zkClient.unsubscribeDataChanges(commandPath, dataListener);
	}

	/**
	 * 监听config节点数据的变化(配置管理,发布)
	 */
	private class MyDataListener implements IZkDataListener {
		/**
		 * 数据改变触发(配置管理,发布)
		 */
		@Override
		public void handleDataChange(String dataPath, Object data) throws Exception {
			String cmd = new String((byte[]) data);
			System.out.println("manage server : cmd = " + cmd);
			exeCmd(cmd);
		}

		@Override
		public void handleDataDeleted(String dataPath) throws Exception {
		}

	}

	/**
	 * 监听server子节点列表的变化(服务发现,订阅)
	 */
	private class MyChildListener implements IZkChildListener {

		@Override
		public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
			workServerList = currentChilds;
			System.out.println("manage server : work server list changed, new list is "+parentPath);
			exeList();
		}

	}

}
