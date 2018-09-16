package com.study._4master;
/**
 * master选举 主工作类
 * @author xmy
 * @time：2018年9月9日 下午3:14:10
 */

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkException;
import org.I0Itec.zkclient.exception.ZkInterruptedException;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.zookeeper.CreateMode;

public class WorkServer {
	/** 服务器是否在运行 */
	private volatile boolean running = false;

	private ZkClient zkClient;
	/** 主节点路径 */
	private static final String MASTER_PATH = "/master";

	/** 订阅节点的子节点内容的变化 */
	private IZkDataListener dataListener;
	/** 从节点 */
	private RunningData serverData;
	/** 主节点 */
	private RunningData masterData;
	/** 延迟执行 */
	private ScheduledExecutorService delayExecutor = Executors.newScheduledThreadPool(1);
	
	 private int delayTime = 5;
	/**
	 * 构造函数
	 * 
	 * @param runningData
	 */

	public WorkServer(RunningData runningData) {
		this.serverData = runningData;
		this.dataListener = new MyZkDataListener();
	}

	/**
	 * 启动服务
	 * @throws Exception 
	 * 
	 */
	public void startServer() throws Exception {
		System.out.println(this.serverData.getName() + " 启动了!");
		
		if(running) {
			throw new Exception("the server is running");
		}
		running = true;
		// 订阅删除事件(监听节点数据变化)
		zkClient.subscribeDataChanges(MASTER_PATH, dataListener);
		takeMaster();
	}

	/**
	 * 关闭服务器
	 * 
	 * @throws Exception
	 */
	public void stopServer() throws Exception {
		if (!running)
			throw new Exception("server has stoped");
		running = false;
		delayExecutor.shutdown();
		
		// 取消订阅删除事件
		zkClient.unsubscribeDataChanges(MASTER_PATH, dataListener);
		
		// 释放master
		releaseMaster();
	}

	/**
	 * 争抢master节点
	 * 
	 */
	public void takeMaster() {
		if (!running) {
			return;
		}
		try {
			// 创建一个临时节点
			zkClient.create(MASTER_PATH, serverData, CreateMode.EPHEMERAL);

			masterData = serverData;
			System.out.println(serverData.getName() + " 选举为:leader");

			// 测试: 5s后判断是否是master节点,是的话 释放master节点
			// 释放后,其他节点都是有监听删除事件的,会争抢master
			delayExecutor.schedule(new Runnable() {

				@Override
				public void run() {
					// 判断,是否是master
					if (checkIsMaster()) {
						System.out.println("模拟leader掉线...");
						releaseMaster();
					}
				}
			}, 5, TimeUnit.SECONDS);

		} catch (ZkNodeExistsException e) {
			RunningData runningData = zkClient.readData(MASTER_PATH, true);
			// 如果读取路径为空 说明master没有建立
			if (runningData == null) {
				// 执行争抢master
				takeMaster();
			} else {
				// master已产生,做赋值
				masterData = runningData;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 释放master
	 * 
	 */
	public void releaseMaster() {
		if (checkIsMaster()) {
			zkClient.delete(MASTER_PATH);
		}
	}

	/**
	 * 检查是否是master
	 * 
	 * @return
	 */
	public boolean checkIsMaster() {
		try {
			RunningData eventData = zkClient.readData(MASTER_PATH);
			masterData = eventData;
			if (masterData.getName().equals(serverData.getName())) {
				return true;
			}
			return false;
		} catch (ZkNoNodeException e) {
			return false;
		} catch (ZkInterruptedException e) {
			return checkIsMaster();
		} catch (ZkException e) {
			return false;
		}
	}

	public ZkClient getZkClient() {
		return zkClient;
	}

	public WorkServer setZkClient(ZkClient zkClient) {
		this.zkClient = zkClient;
		return this;
	}

	private class MyZkDataListener implements IZkDataListener {
		@Override
		public void handleDataDeleted(String dataPath) throws Exception {
			takeMaster();
			// 对应网络抖动的方法
			// 由于网络抖动，可能误删了master节点导致重新选举，如果master还未宕机，而被其他节点抢到了，
			// 会造成可能有写数据重新生成等资源的浪费。我们这里，增加一个判断，如果上次自己不是master就等待5s在开始争抢master，
			// 这样就能保障没有宕机的master能再次选中为master。
			/*
			if(masterData != null && masterData.getName().equals(serverData.getName())) {
				takeMaster();
			}else {
				delayExecutor.schedule(new Runnable() {
					
					@Override
					public void run() {
						takeMaster();
					}
				}, delayTime, TimeUnit.SECONDS);
			}
			*/

		}

		@Override
		public void handleDataChange(String dataPath, Object data) throws Exception {
		}

	}

}
