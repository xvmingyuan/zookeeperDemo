package com.study._1api;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

/**
 * 建立连接
 * 
 * @author xmy
 */
public class _1CreateSession implements Watcher {

	private static ZooKeeper zk;

	@Override
	public void process(WatchedEvent we) {
		System.out.println("收到事件" + we);
		if (we.getState() == KeeperState.SyncConnected) {
			doSomething();
		}
	}

	private void doSomething() {
		System.out.println("do something");
	}

	public static void main(String[] args) throws Exception {
		// 需要传递一个事件监听器，通过事件监听器来介绍zk的事件通知,实现watcher接口,(当前使用本类实现接口)
		zk = new ZooKeeper("it01:2181", 5000, new _1CreateSession());

		// 获取zk状态并输出事件接收到的数据
		System.out.println("zk state:" + zk.getState());
		// 因为main还没等到建立好连接就执行完退出了
		// 需要sleep,下面的监听事件才可以执行
		Thread.sleep(Integer.MAX_VALUE);
	}
	/*打印结果:
	  zk state:CONNECTING 
	  收到事件WatchedEvent state:SyncConnected type:None path:null
	  do something
	 */
}
