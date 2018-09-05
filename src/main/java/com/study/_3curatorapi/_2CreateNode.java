package com.study._3curatorapi;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.CreateMode;

public class _2CreateNode {
	public static void main(String[] args) throws Exception {
		// retryPolicy 重试策略
		
		// 刚开始的,重试时间间隔是1000ms，后面一直增加，最多不超过三次
//		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		// 最多重试5次，每次停顿1000ms
//		RetryPolicy retryPolicy = new RetryNTimes(5, 1000);
		// 重试过程不能超过5000ms，每次间隔1000s
		RetryPolicy retryPolicy  = new RetryUntilElapsed(5000, 1000);
		
//		CuratorFramework client = CuratorFrameworkFactory.newClient("it01:2181",5000,5000, retryPolicy);
		
		// Fluent风格
		CuratorFramework client = CuratorFrameworkFactory
			.builder()
			.connectString("it01:2181")
			.sessionTimeoutMs(5000)
			.connectionTimeoutMs(5000)
			.retryPolicy(retryPolicy)
			.build();
		
		client.start();
		
		String path = client.create()
			  .creatingParentsIfNeeded()
			  .withMode(CreateMode.EPHEMERAL)
			  .forPath("/node4","4".getBytes());
		System.out.println(path);
		Thread.sleep(Integer.MAX_VALUE);

	}

}
