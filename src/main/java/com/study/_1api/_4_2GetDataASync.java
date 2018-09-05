package com.study._1api;

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
public class _4_2GetDataASync implements Watcher {

	private static ZooKeeper zk;

	public static void main(String[] args) throws Exception {
		zk = new ZooKeeper("it01:2181", 5000, new _4_2GetDataASync());
		System.out.println("zk state:" + zk.getState());
		Thread.sleep(Integer.MAX_VALUE);
	}

	@Override
	public void process(WatchedEvent we) {
		if (we.getState() == KeeperState.SyncConnected) {
			if (we.getType() == EventType.None && null == we.getPath()) {
				doSomething(zk);
			} else {
				if (we.getType() == EventType.NodeDataChanged) {
					try {
						zk.getData(we.getPath(), true, new IDataCallback(), null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void doSomething(ZooKeeper zk) {
		zk.getData("/node2", true, new IDataCallback(), null);
	}

	static class IDataCallback implements AsyncCallback.DataCallback {

		@Override
		public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
			try {
				System.out.println(new String(zk.getData(path, true, stat)));
				System.out.println("stat:" + stat);
			} catch (KeeperException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	/*
	 * 打印结果:
	 *  zk state:CONNECTING
		123
		stat:133143986207,133143986207,1535944246776,1535944246776,0,2,0,0,3,0,133143986224
	 * 
	 */
}
