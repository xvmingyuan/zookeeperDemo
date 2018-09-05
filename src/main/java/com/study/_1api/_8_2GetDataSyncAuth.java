package com.study._1api;

import java.io.IOException;

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
public class _8_2GetDataSyncAuth implements Watcher {

	private static ZooKeeper zooKeeper;
	private static Stat stat = new Stat();

	public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
		zooKeeper = new ZooKeeper("it01:2181", 5000, new _8_2GetDataSyncAuth());
		Thread.sleep(Integer.MAX_VALUE);
	}

	private void doSomething(ZooKeeper zookeeper) {
		zooKeeper.addAuthInfo("digest", "admin:admin".getBytes());
		try {
			System.out.println(new String(zooKeeper.getData("/node1", true, stat)));
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process(WatchedEvent event) {
		if (event.getState() == KeeperState.SyncConnected) {
			if (event.getType() == EventType.None && null == event.getPath()) {
				doSomething(zooKeeper);
			} else {
				if(event.getType() == EventType.NodeChildrenChanged) {
					try {
						System.out.println(new String(zooKeeper.getData("/node1", true, stat)));
						System.out.println("stat:" + stat);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
		}
	}

}
