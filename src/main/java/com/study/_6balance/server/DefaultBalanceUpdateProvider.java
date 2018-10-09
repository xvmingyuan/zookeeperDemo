package com.study._6balance.server;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkBadVersionException;
import org.apache.zookeeper.data.Stat;

public class DefaultBalanceUpdateProvider implements BalanceUpdateProvider {
	private String serverPath;
	private ZkClient zkClient;

	public DefaultBalanceUpdateProvider(String serverPath, ZkClient zkClient) {
		this.serverPath = serverPath;
		this.zkClient = zkClient;
	}

	/**
	 * 添加客户端连接
	 */
	@Override
	public boolean addBalance(Integer step) {
		Stat stat = new Stat();
		ServerData serverData;
		while (true) {
			try {
				serverData = zkClient.readData(this.serverPath, stat);
				serverData.setBalance(serverData.getBalance() + step);
				System.out.println("server status:"+serverData+" version:"+stat.getVersion());
				zkClient.writeData(this.serverPath, serverData, stat.getVersion());
				return true;
			} catch (ZkBadVersionException e) {
				//e.printStackTrace();
			} catch (Exception e) {
				return false;
			}

		}

	}

	/**
	 * 释放客户端连接
	 */
	@Override
	public boolean reduceBalance(Integer step) {
		Stat stat = new Stat();
		ServerData serverData;
		while (true) {
			try {
				serverData =  zkClient.readData(this.serverPath, stat);
				final Integer currBalance = serverData.getBalance();
				serverData.setBalance(currBalance > step ? currBalance-step:0);
				zkClient.writeData(this.serverPath, serverData, stat.getVersion());
				return true;
				
			} catch (ZkBadVersionException e) {
				//e.printStackTrace();
			}
			catch (Exception e) {
				return false;
			}
		}

	}

}
