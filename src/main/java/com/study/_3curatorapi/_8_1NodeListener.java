package com.study._3curatorapi;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.RetryUntilElapsed;

public class _8_1NodeListener {
	static NodeCache cache = null;

	public static void main(String[] args) throws Exception {
		RetryPolicy retryPolicy = new RetryUntilElapsed(5000, 1000);
		// Fluent风格
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString("it01:2181").sessionTimeoutMs(5000)
				.connectionTimeoutMs(5000).retryPolicy(retryPolicy).build();

		client.start();
		cache = new NodeCache(client, "/node3");
		cache.start();
		cache.getListenable().addListener(new INodeCacheListener());

		Thread.sleep(Integer.MAX_VALUE);

	}

	private static class INodeCacheListener implements NodeCacheListener {
		
		@Override
		public void nodeChanged() throws Exception {
			byte[] ret = cache.getCurrentData().getData();
			System.out.println("new data:" + new String(ret));

		}

	}

}
