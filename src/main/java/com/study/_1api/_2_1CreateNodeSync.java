package com.study._1api;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * 创建节点(同步)
 * 
 * @author xmy
 */
public class _2_1CreateNodeSync implements Watcher {
	private static ZooKeeper zk;

	public static void main(String[] args) throws Exception {
		zk = new ZooKeeper("it01:2181", 5000, new _2_1CreateNodeSync());
		System.out.println("zk state:" + zk.getState());
		Thread.sleep(Integer.MAX_VALUE);
	}

	@Override
	public void process(WatchedEvent we) {
		System.out.println("收到事件" + we);
		if (we.getState() == KeeperState.SyncConnected) {
			doSomething();
		}
	}

	private void doSomething() {
		try {
			// Ids.OPEN_ACL_UNSAFE 任何人可以对这个节点进行任何操作
			String path = zk.create("/node2", "123".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			System.out.println(path);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("doSomething");
	}
	/*结果
	zk state:CONNECTING
	收到事件WatchedEvent state:SyncConnected type:None path:null
	/node2
	doSomething 
	*/
}
