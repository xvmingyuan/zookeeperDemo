package com.study._1api;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.ZooKeeper;

/**
 * 
 * @author xmy
 */
public class _4_1GetDataSync implements Watcher {

	private static ZooKeeper zk;
	private static Stat stat = new Stat();
	
	public static void main(String[] args) throws Exception {
		zk = new ZooKeeper("it01:2181", 5000, new _4_1GetDataSync());
		System.out.println("zk state:" + zk.getState());
		Thread.sleep(Integer.MAX_VALUE);
	}

	@Override
	public void process(WatchedEvent we) {
		if(we.getState() == KeeperState.SyncConnected) {
			if(we.getType() == EventType.None && null == we.getPath()) {
				doSomething(zk);
			}else {
				if(we.getType() == EventType.NodeChildrenChanged) {
					try {
						System.out.println(new String(zk.getData(we.getPath(), true, stat)));
						System.out.println("stat:" + stat);
					} catch (KeeperException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
				
			}
		}
		
	}

	private void doSomething(ZooKeeper zookeeper) {
		// zookeeper.addAuthInfo("digest", "jerome:123456".getBytes());
		try {
			// true false 要不要关注这个节点的子节点的变化
			System.out.println(new String(zookeeper.getData("/node2", true, stat)));
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/*
	 * 打印结果:
	 *   zk state:CONNECTING
		 123
	 * 
	 */
}
