package com.study._6balance.client;
/**
 * 默认负载均衡算法实现
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import com.study._6balance.server.ServerData;

public class DefaultBalanceProvider extends AbstractBalanceProvider<ServerData> {
	private final String zkServer;// zookeeper服务器地址
	private final String serversPath;// servers节点路径
	private final ZkClient zkClient;

	private static final Integer SESSION_TIME_OUT = 10000;
	private static final Integer CONNECT_TIME_OUT = 10000;
	
	public DefaultBalanceProvider(String zkServer, String serversPath) {
		this.zkServer = zkServer;
		this.serversPath = serversPath;
		this.zkClient = new ZkClient(this.zkServer, SESSION_TIME_OUT,CONNECT_TIME_OUT,new SerializableSerializer());
	}

	@Override
	protected ServerData balanceAlgorithm(List<ServerData> items) {
		if(items.size()>0) {
//			System.out.println("DefaultBalanceProvider.java 负载算法实现: "+items.toString());
			Collections.sort(items);// 根据负载由小到大排序
			return items.get(0);// 返回负载最小的
		}else {
			return null;
		}
		
	}
	 /**
     * 从zookeeper中拿到所有工作服务器的基本信息
     */
	@Override
	protected List<ServerData> getBalanceItems() {
		List<ServerData> sdList = new ArrayList<ServerData>();
		List<String> children = zkClient.getChildren(this.serversPath);
		for (int i = 0; i < children.size(); i++) {
			ServerData serverData = zkClient.readData(serversPath+"/"+children.get(i));
//			System.out.println("DefaultBalanceProvider.java serversPath: "+serversPath+"/"+children.get(i));
//			System.out.println("DefaultBalanceProvider.java serverData: "+serverData);
			sdList.add(serverData);
		}
		return sdList;
	}

}
