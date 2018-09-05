package com.study._1api;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;

/**
 * 创建节点(异步)
 * 
 * @author xmy
 */
public class _2_2CreateNodeASync implements Watcher {

	private static ZooKeeper zk;

	public static void main(String[] args) throws Exception {
		zk = new ZooKeeper("it01:2181", 5000, new _2_2CreateNodeASync());
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
		/**
		 * @param path(路径) data(节点携带数据) ACL(权限) model(创建模式) class(异步实现类) classString(异步传入的ctx内容)
		 */
		zk.create("/node22", "2_2".getBytes(), Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT, new IStringCallBack(),"Create 22");
	}

	static class IStringCallBack implements AsyncCallback.StringCallback {
		/**
		 * @param rc
		 *            返回码0表示成功
		 * @param path
		 *            我们需要创建的节点的完整路径
		 * @param ctx
		 *            上面传入的值("创建")
		 * @param name
		 *            服务器返回给我们已经创建的节点的真实路径,如果是顺序节点path和name是不一样的
		 */
		@Override
		public void processResult(int rc, String path, Object ctx, String name) {
			StringBuffer sb = new StringBuffer();
			sb.append("rc=" + rc).append("\n");
			sb.append("path=" + path).append("\n");
			sb.append("ctx=" + ctx).append("\n");
			sb.append("name=" + name);
			System.out.println(sb.toString());
		}

	}
//	结果
//	zk state:CONNECTING
//	收到事件WatchedEvent state:SyncConnected type:None path:null
//	rc=0
//	path=/node22
//	ctx=Create 22
//	name=/node22
}
