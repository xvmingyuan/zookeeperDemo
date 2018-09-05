package com.study._1api;

import java.util.List;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

/**
 * 
 * @author xmy
 */
public class _3_1GetChildrenSync implements Watcher {

	private static ZooKeeper zk;

	public static void main(String[] args) throws Exception {
		zk = new ZooKeeper("it01:2181", 5000, new _3_1GetChildrenSync());
		System.out.println("zk state:" + zk.getState());
		Thread.sleep(Integer.MAX_VALUE);
	}

	@Override
	public void process(WatchedEvent we) {
		// 保证客户端与服务端建立连接后 Dosomething的内容只执行一次
		if (we.getState() == KeeperState.SyncConnected) {
			if (we.getType() == EventType.None && null == we.getPath()) {
				doSomething();
			} else {
				// doSomething 有关注节点的变化
				if (we.getType() == EventType.NodeChildrenChanged) {
					try {
						System.out.println(zk.getChildren(we.getPath(), true));
					} catch (Exception e) {
					}

				}
			}

		}
	}

	private void doSomething() {
		try {
			// 返回节点下面的所有子节点的列表
			// true false 要不要关注这个节点的子节点的变化
			List<String> children = zk.getChildren("/", true);
			System.out.println(children);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	/*
	 * 打印结果:
	 * 
	 * 
	 */
}
