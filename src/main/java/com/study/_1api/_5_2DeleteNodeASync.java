package com.study._1api;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

/**
 * 
 * @author xmy
 */
public class _5_2DeleteNodeASync implements Watcher {
	private static ZooKeeper zk;

	public static void main(String[] args) throws Exception {
		zk = new ZooKeeper("it01:2181", 5000, new _5_2DeleteNodeASync());
		Thread.sleep(Integer.MAX_VALUE);
	}

	@Override
	public void process(WatchedEvent event) {
		if (event.getState() == KeeperState.SyncConnected) {
			if (event.getType() == EventType.None && null == event.getPath()) {
				doSomething(zk);
			}
		}
	}

	private void doSomething(ZooKeeper zk) {
		try {
			// -1表示不校验版本信息
			zk.delete("/node2/node220000000002", -1,new IVoidCallback(), "异步删除");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	static class IVoidCallback implements AsyncCallback.VoidCallback{

		@Override
		public void processResult(int rc, String path, Object ctx) {
			StringBuilder sb = new StringBuilder();
			sb.append("rc=" + rc).append("\n");
			sb.append("path=" + path).append("\n");
			sb.append("ctx=" + ctx).append("\n");
			System.out.println(sb.toString());
		}
		
	}
}
