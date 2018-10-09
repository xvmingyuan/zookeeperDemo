package com.study._6balance.server;
/**
 * Zookeeper注册内容
 * @author xmy
 * @time：2018年9月22日 下午3:18:54
 */

import org.I0Itec.zkclient.ZkClient;

public class ZooKeeperRegistContext {
	private String path;
	private ZkClient zkClient;
	private Object data;
	
	public ZooKeeperRegistContext(String path, ZkClient zkClient, Object data) {
		this.path = path;
		this.zkClient = zkClient;
		this.data = data;
	}
	public String getPath() {
		return path;
	}
	public ZkClient getZkClient() {
		return zkClient;
	}
	public Object getData() {
		return data;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public void setZkClient(ZkClient zkClient) {
		this.zkClient = zkClient;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
}
