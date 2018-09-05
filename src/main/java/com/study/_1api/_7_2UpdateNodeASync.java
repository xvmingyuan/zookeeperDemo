package com.study._1api;

import java.io.IOException;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.KeeperException;
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
public class _7_2UpdateNodeASync implements Watcher {

	private static ZooKeeper zooKeeper;

	public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
		zooKeeper = new ZooKeeper("it01:2181", 5000, new _7_2UpdateNodeASync());
		Thread.sleep(Integer.MAX_VALUE);
	}

	private void doSomething(WatchedEvent event) {
		zooKeeper.setData("/node2", "234".getBytes(), -1, new IStatCallback(), null);
	}

	@Override
	public void process(WatchedEvent event) {
		if (event.getState() == KeeperState.SyncConnected) {
			if (event.getType() == EventType.None && null == event.getPath()) {
				doSomething(event);
			}
		}
	}

	static class IStatCallback implements AsyncCallback.StatCallback {

		@Override
		public void processResult(int rc, String path, Object ctx, Stat stat) {
			StringBuilder sb = new StringBuilder();
			sb.append("rc=" + rc).append("\n");
			sb.append("path" + path).append("\n");
			sb.append("ctx=" + ctx).append("\n");
			sb.append("Stat=" + stat).append("\n");
			System.out.println(sb.toString());
		}
	}

}
