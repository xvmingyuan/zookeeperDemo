package com.study._1api;

import java.util.ArrayList;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;


/**
 * 
 * @author xmy
 */
public class _8_1CreateNodeSyncAuth implements Watcher {
	private static ZooKeeper zooKeeper;
	private static boolean somethingDone = false;

	public static void main(String[] args) throws Exception {
		zooKeeper = new ZooKeeper("it01:2181", 5000, new _8_1CreateNodeSyncAuth());
		Thread.sleep(Integer.MAX_VALUE);
//		System.out.println(DigestAuthenticationProvider.generateDigest("admin:admin"));
	}
	@Override
	public void process(WatchedEvent event) {
		if (event.getState() == KeeperState.SyncConnected) {
			if (!somethingDone && event.getType() == EventType.None && null == event.getPath()) {
				doSomething();
			}
		}
	}
	/**
	 * 权限模式(scheme): ip, digest
	 * 授权对象(ID)
	 * 		ip权限模式:  具体的ip地址
	 * 		digest权限模式: username:Base64(SHA-1(username:password))
	 * 权限(permission): create(C), DELETE(D),READ(R), WRITE(W), ADMIN(A) 
	 * 		注：单个权限，完全权限，复合权限
	 * 
	 * 权限组合: scheme + ID + permission
	 * 
	 */
	private void doSomething() {
		try {
			// /zkCli.sh -server 192.168.1.129:2181
			ACL aclIp = new ACL(Perms.READ,new Id("ip", "192.168.1.129"));
			// 执行:addauth digest admin:admin
			ACL aclDigest = new ACL(Perms.READ | Perms.WRITE,new Id("digest", DigestAuthenticationProvider.generateDigest("admin:admin")));
			ArrayList<ACL> acls = new  ArrayList<ACL>();
			acls.add(aclIp);
			acls.add(aclDigest);
			String path = zooKeeper.create("/node1", "111".getBytes(), acls, CreateMode.PERSISTENT);
			System.out.println("return path:" + path);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
