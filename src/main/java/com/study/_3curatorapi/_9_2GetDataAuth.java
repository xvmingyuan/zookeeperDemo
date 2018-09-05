package com.study._3curatorapi;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.data.Stat;

public class _9_2GetDataAuth {
	public static void main(String[] args) throws Exception {
		RetryPolicy retryPolicy = new RetryUntilElapsed(5000, 1000);

		// Fluent风格
		CuratorFramework client = CuratorFrameworkFactory
				.builder()
				.connectString("it01:2181")
				.sessionTimeoutMs(5000)
				.authorization("digest", "admin:admin".getBytes())
				.connectionTimeoutMs(5000)
				.retryPolicy(retryPolicy)
				.build();

		client.start();

		Stat stat = new Stat();
		byte[] res = client.getData().storingStatIn(stat).forPath("/node5");
		System.out.println(new String(res));

		Thread.sleep(Integer.MAX_VALUE);

	}

}
