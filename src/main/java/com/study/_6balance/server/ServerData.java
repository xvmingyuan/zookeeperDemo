package com.study._6balance.server;

import java.io.Serializable;
/**
 * 服务器和客户端公用的类，计算负载等使用
 * @author xmy
 * @time：2018年9月20日 下午10:32:00
 */
public class ServerData implements Serializable,Comparable<ServerData>{

	private static final long serialVersionUID = -4326829699636520640L;
	
	public Integer balance;
	public String host;
	public Integer port;
	
	public Integer getBalance() {
		return balance;
	}

	public String getHost() {
		return host;
	}

	public Integer getPort() {
		return port;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(Integer port) {
		this.port = port;
	}
	
	@Override
	public String toString() {
		return "ServerData [balance=" + balance + ", host=" + host + ", port=" + port + "]";
	}

	@Override
	public int compareTo(ServerData o) {
		return this.getBalance().compareTo(o.getBalance());
	}

}
