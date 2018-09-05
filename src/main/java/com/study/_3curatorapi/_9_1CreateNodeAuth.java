package com.study._3curatorapi;

import java.util.ArrayList;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

public class _9_1CreateNodeAuth {
	public static void main(String[] args) throws Exception {
		RetryPolicy retryPolicy = new RetryUntilElapsed(5000, 1000);


		// Fluent风格
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString("it01:2181").sessionTimeoutMs(5000)
				.connectionTimeoutMs(5000).retryPolicy(retryPolicy).build();

		client.start();
		
		ACL aclIp = new ACL(Perms.READ,new Id("ip", "192.168.1.129"));
		ACL aclDigest = new ACL(Perms.READ | Perms.WRITE,new Id("digest",DigestAuthenticationProvider.generateDigest("admin:admin")));
		ArrayList<ACL> aclList = new ArrayList<ACL>();
		aclList.add(aclIp);
		aclList.add(aclDigest);
		
		String path = client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).withACL(aclList).forPath("/node5","5".getBytes());
		System.out.println(path);
		Thread.sleep(Integer.MAX_VALUE);

	}



}
