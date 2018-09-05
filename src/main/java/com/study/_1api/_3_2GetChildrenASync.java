package com.study._1api;

import java.util.List;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * 
 * @author xmy
 */
public class _3_2GetChildrenASync implements Watcher {

	private static ZooKeeper zk;

	public static void main(String[] args) throws Exception {
		zk = new ZooKeeper("it01:2181", 5000, new _3_2GetChildrenASync());
		System.out.println("zk state:" + zk.getState());
		Thread.sleep(Integer.MAX_VALUE);
	}

	@Override
	public void process(WatchedEvent we) {
		// 保证客户端与服务端建立连接后 Dosomething的内容只执行一次
		if (we.getState() == KeeperState.SyncConnected) {
			if (we.getType() == EventType.None && null == we.getPath()) {
				doSomething(zk);
			} else {
				// doSomething 有关注节点的变化
				if (we.getType() == EventType.NodeChildrenChanged) {
					try {
						zk.getChildren(we.getPath(), true, new IChildren2Callback(),null);
					} catch (Exception e) {
					}

				}
			}

		}
	}

	private void doSomething(ZooKeeper zk) {
		try {
			zk.getChildren("/", true,new IChildren2Callback(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static class IChildren2Callback implements AsyncCallback.Children2Callback {

		@Override
		public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
			StringBuilder sb = new StringBuilder();
			sb.append("rc=" + rc).append("\n");
			sb.append("path=" + path).append("\n");
			sb.append("ctx=" + ctx).append("\n");
			sb.append("children=" + children).append("\n");
			sb.append("stat=" + stat).append("\n");
			System.out.println(sb.toString());
		}
	}

	/*
	 * 打印结果:
	 * 
	 * 
	 */
}
