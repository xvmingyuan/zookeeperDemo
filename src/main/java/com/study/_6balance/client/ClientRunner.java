package com.study._6balance.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.study._6balance.server.ServerData;

/**
 * 
 * @author xmy
 * @time：2018年9月20日 下午6:01:50
 */
public class ClientRunner {
	/** 启动客户端的个数 */
	private static final int CLIENT_QTY = 10;
	/** Zookeeper服务器地址 */
	private static final String ZOOKEEPER_SERVER = "it01:2181";
	/** Zookeeper服务器注册的节点 */
	private static final String SERVERS_PATH = "/servers";

	public static void main(String[] args) {
		List<Thread> threadList = new ArrayList<Thread>(CLIENT_QTY);
		final List<Client> clientList = new ArrayList<Client>();
		// 实例化负载器接口
		final BalanceProvider<ServerData> balanceProvider = new DefaultBalanceProvider(ZOOKEEPER_SERVER, SERVERS_PATH);
		try {
			for (int i = 0; i < CLIENT_QTY; i++) {
				
				// 一.同步方式(后期可以改为使用分布式队列替换)
//				Client client = new ClientImpl(balanceProvider);
//				clientList.add(client);
//				try {
//					//客户端连接服务器
//					client.connect();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
				
				// 二.使用线程方式(异步争抢服务器资源,服务器必须保证同步或做同步机制处理,保证一个客户端只占用一个服务器资源:ServerHandler.java)
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Client client = new ClientImpl(balanceProvider);
							clientList.add(client);
							//客户端连接服务器
							client.connect();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				threadList.add(thread);
				thread.start();
				// 延时
				Thread.sleep(3000);
			}
			System.out.println("敲回车键退出！\n");
			new BufferedReader(new InputStreamReader(System.in)).readLine();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭客户端
			for (int i = 0; i < clientList.size(); i++) {
				try {
					Thread.sleep(2000);
					clientList.get(i);
					clientList.get(i).disConnect();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// 关闭线程
			for (int i = 0; i < threadList.size(); i++) {
				threadList.get(i).interrupt();
				try {
					threadList.get(i).join();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
