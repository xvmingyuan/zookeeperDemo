package com.count;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class BasicDemo {
	public static String zkIp = "it01:2181";
	public static int outtime = 60000;
	public static void main(String[] args) throws IOException,KeeperException,InterruptedException{
//		// 创建一个与服务器的连接
		ZooKeeper zK = new ZooKeeper(zkIp, outtime, new IWatcher());
		// 查看节点
		System.out.println("ls =>"+zK.getChildren("/queue", false));
		List<String> children = zK.getChildren("/queue", false);
		for (String child : children) {
			zK.delete("/queue"+"/"+child, -1);
		}
		System.out.println("ls =>"+zK.getChildren("/queue", false));
		zK.close();
	}
	
	// 监控所有被触发的事件
	private static class IWatcher implements Watcher{
		@Override
		public void process(WatchedEvent event) {
			System.out.println("EVENT:" + event.getType());
		}
		
	}
}
