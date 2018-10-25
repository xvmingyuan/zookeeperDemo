package com.count;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
/**
 * 顺序队列Demo
 * @author xmy
 * @time：2018年10月25日 下午9:53:26
 */
public class QueueZookeeper {
	public static String START = "/queue/start";
	public static String QUEUE = "/queue";
	public static String X = "/queue/x";
	public static int size = 3;
	public static int sessionTimeout = 60000;

	// main
	public static void main(String[] args) throws Exception {
		String[] args2 = {}; 
		args = args2;
		if (args.length == 0) {
            doOne();
        } else {
            doAction(Integer.parseInt(args[0]));
        }
	}

	// doOne
	public static void doOne() throws Exception {
		String host1 = "it01:2181";
		ZooKeeper zk = connection(host1, sessionTimeout);
		initQueue(zk);
		joinQueue(zk, 1);
		joinQueue(zk, 2);
		joinQueue(zk, 3);
		zk.close();
	}

	// doction
	public static void doAction(int client) throws Exception {
		String host1 = "it01:2181";
		String host2 = "it02:2181";
		String host3 = "it03:2181";
		ZooKeeper zk = null;
		switch (client) {
		case 1:
			zk = connection(host1, sessionTimeout);
			initQueue(zk);
			joinQueue(zk, 1);
			break;
		case 2:
			zk = connection(host2, sessionTimeout);
			initQueue(zk);
			joinQueue(zk, 2);
			break;
		case 3:
			zk = connection(host3, sessionTimeout);
			initQueue(zk);
			joinQueue(zk, 3);
			break;
		}
	}

	// 创建一个与服务器的连接
	public static ZooKeeper connection(String host, int sessionTimeout) throws IOException {
		ZooKeeper zk = new ZooKeeper(host, sessionTimeout, new IWather());
		return zk;
	}

	// 初始化队列
	public static void initQueue(ZooKeeper zk) throws KeeperException, InterruptedException {
		System.out.println("WATCH => /queue/start");
		zk.exists(START, true);
		if ((zk.exists(QUEUE, false)) == null) {
			System.out.println("create /queue task-queue");
			// 创建固定节点
			zk.create(QUEUE, "task-queue".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} else {
			System.out.println("/queue is exist!");
		}
	}

	// 加入队列
	public static void joinQueue(ZooKeeper zk, int x) throws KeeperException, InterruptedException {
		System.out.println("create /queue/x" + x + " x" + x);
		// 创建顺序节点
		zk.create(X + x, ("x" + x).getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		isFinished(zk);
	}

	// 判断队列是否完成
	public static void isFinished(ZooKeeper zk) throws KeeperException, InterruptedException {
		int length = zk.getChildren(QUEUE, true).size();
		System.out.println("Queue Complete:" + length + "/" + size);
		if (length >= size) {
			System.out.println("create /queue/start start");
			// 创建固定节点
			zk.create(START, "start".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		}
	}

	// 内部监听类
	public static class IWather implements Watcher {
		// 监控所有被触发的事件
		@Override
		public void process(WatchedEvent event) {
			if (event.getType() == Event.EventType.NodeCreated && event.getPath().equals(START)) {
				System.out.println("Queue has Completed.Finish testing!!!");
			}
		}

	}
}
