package com.count.hadoop;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.DELETE;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * Zookeeper调度工作类
 * 
 * @author xmy
 * @time：2018年10月25日 下午9:53:06
 */
public class ZookeeperJob {
	// final 静态参数
	final public static String QUEUE = "/queue";
	final public static String PROFIT = "/queue/profit";
	final public static String PURCHASE = "/queue/purchase";
	final public static String SELL = "/queue/sell";
	final public static String OTHER = "/queue/other";
	public static int size = 3;
	public static int sessionTimeout = 60000;

	// main
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			System.out.println("Please start a task:");
		} else {
			doAction(Integer.parseInt(args[0]));
		}

	}

	// doAction
	public static void doAction(int client) throws Exception {
		String host1 = "it01:2181";
		String host2 = "it02:2181";
		String host3 = "it03:2181";
		ZooKeeper zk = null;
		switch (client) {
		case 1:
			zk = connection(host1, sessionTimeout);
			initQueue(zk);
			doPurchase(zk);
			break;
		case 2:
			zk = connection(host2, sessionTimeout);
			initQueue(zk);
			doSell(zk);
			break;
		case 3:
			zk = connection(host3, sessionTimeout);
			initQueue(zk);
			doOther(zk);
			break;
		}

	}

	// 创建一个与服务器的连接 connection
	public static ZooKeeper connection(String host, int sessionTimeout) throws IOException {
		ZooKeeper zk = new ZooKeeper(host, sessionTimeout, new IWatcher());
		return zk;
	}

	// initQueue
	public static void initQueue(ZooKeeper zk) throws KeeperException, InterruptedException {
		System.out.println("WATCH => " + PROFIT);
		zk.exists(PROFIT, true);
		if (zk.exists(QUEUE, false) == null) {
			System.out.println("create " + QUEUE);
			// 创建固定节点
			zk.create(QUEUE, "create Queue".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} else {
			System.out.println(QUEUE + " is existed!");
		}
	}

	// doPurchase
	public static void doPurchase(ZooKeeper zk) throws Exception {
		if (zk.exists(PURCHASE, false) == null) {
			Purchase.run(Purchase.getPath());
			System.out.println("create " + PURCHASE);
			zk.create(PURCHASE, PURCHASE.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} else {
			System.out.println(PURCHASE + " is existed!");
		}
		isFinished(zk);

	}

	// doSell
	public static void doSell(ZooKeeper zk) throws Exception {
		if (zk.exists(SELL, false) == null) {
			Sell.run(Sell.getPath());
			System.out.println("create " + SELL);
			zk.create(SELL, SELL.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} else {
			System.out.println(SELL + " is existed!");
		}
		isFinished(zk);

	}

	// doOther
	public static void doOther(ZooKeeper zk) throws Exception {
		if (zk.exists(OTHER, false) == null) {
			Other.calcOther(Other.file);
			System.out.println("create " + OTHER);
			zk.create(OTHER, OTHER.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} else {
			System.out.println(OTHER + " is existed!");
		}
		isFinished(zk);
	}

	// isFinished
	public static void isFinished(ZooKeeper zk) throws Exception {
		List<String> children = zk.getChildren(QUEUE, true);
		int length = children.size();
		System.out.println("Queue Complete:" + length + "/" + size);
		if (length >= size && 
				zk.exists(PURCHASE, false)!=null && 
				zk.exists(SELL, false) !=null && 
				zk.exists(OTHER, false)!=null) {
			System.out.println("create " + PROFIT);
			Profit.profit();
			zk.create(PROFIT, PROFIT.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		}
	}

	// 监控所有被触发的事件
	public static class IWatcher implements Watcher {
		@Override
		public void process(WatchedEvent event) {
			if (event.getType() == Event.EventType.NodeCreated && event.getPath().equals(PROFIT)) {
				System.out.println("Queue has Completed!!!");
				try {
					deleteAll();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 清空节点
	public static void deleteAll() throws Exception {
		ZooKeeper zk = connection("it01:2181", sessionTimeout);
		List<String> children = zk.getChildren(QUEUE, true);
		for (String child : children) {
			zk.delete(QUEUE + "/" + child, -1);
		}
		zk.close();
	}

}
