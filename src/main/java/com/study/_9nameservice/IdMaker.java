package com.study._9nameservice;
/**
 * 分布式的id生成器
 * @author xmy
 * @time：2018年10月9日 下午9:48:42
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.print.attribute.standard.MediaSize.NA;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

public class IdMaker {

	private ZkClient client = null;
	/** 记录Zookeeper服务器的地址 */
	private final String server;
	/** 记录父节点的路径 */
	private final String root;
	/** 顺序节点的名称前缀 */
	private final String nodeName;
	/** 标识当前服务是否运行 */
	private volatile boolean isRunning = false;
	/** 使用线程池 */
	private ExecutorService cleanExecutor = null;

	public IdMaker(String server, String root, String nodeName) {
		this.server = server;
		this.root = root;
		this.nodeName = nodeName;
	}

	public void start() throws Exception {
		if (isRunning) {
			throw new Exception("server has stated...");
		}
		isRunning = true;
		init();
	}

	private void init() {
		client = new ZkClient(server, 5000, 5000, new SerializableSerializer());
		// 实例化线程池
		cleanExecutor = Executors.newFixedThreadPool(10);
		try {
			client.createPersistent(root, true);
		} catch (ZkNoNodeException e) {
			e.printStackTrace();
		}

	}

	public void stop() throws Exception {
		if (!isRunning)
			throw new Exception("server has stopped...");
		isRunning = false;
		releaseResource();
	}

	private void releaseResource() {
		// 释放线程池
		cleanExecutor.shutdown();
		try {
			cleanExecutor.awaitTermination(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			cleanExecutor = null;
		}
		if (client != null) {
			client.close();
			client = null;
		}
	}

	public String generateId(RemoveMethodEnum removeMethodEnum) throws Exception {
		final String fullNodePath = root.concat("/").concat(nodeName);
		// 创建持久顺序节点
		final String ourPath = client.createPersistentSequential(fullNodePath, null);
		if (removeMethodEnum.equals(RemoveMethodEnum.IMMEDIATELY)) {
			client.delete(ourPath);
		} else if (removeMethodEnum.equals(RemoveMethodEnum.DELAY)) {
			cleanExecutor.execute(new Runnable() {
				@Override
				public void run() {
					client.delete(ourPath);
				}
			});
		}
		return ExtractId(ourPath);

	}

	/**
	 * 抽取ID
	 *
	 * @param str
	 * @return
	 */
	private String ExtractId(String str) {
		int index = str.lastIndexOf(nodeName);
		if (index >= 0) {
			index += nodeName.length();
			return index <= str.length() ? str.substring(index) : "";
		}
		return str;
	}
}
