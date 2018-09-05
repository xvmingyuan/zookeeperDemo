package com.study._3curatorapi;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryUntilElapsed;

public class _8_2NodeChildrenListener {
	private static PathChildrenCache cache;

	public static void main(String[] args) throws Exception {
		RetryPolicy retryPolicy = new RetryUntilElapsed(5000, 1000);

		// Fluent风格
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString("it01:2181").sessionTimeoutMs(5000)
				.connectionTimeoutMs(5000).retryPolicy(retryPolicy).build();

		client.start();

		cache = new PathChildrenCache(client, "/node3", true);
		cache.start();
		cache.getListenable().addListener(new IPathChildrenCacheListener());
		Thread.sleep(Integer.MAX_VALUE);

	}

	private static class IPathChildrenCacheListener implements PathChildrenCacheListener {

		@Override
		public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
			switch (event.getType()) {
			case CHILD_ADDED:
				System.out.println("CHILD_ADDED:" + event.getData());
				break;
			case CHILD_UPDATED:
				System.out.println("CHILD_UPDATED:" + event.getData());
				break;
			case CHILD_REMOVED:
				System.out.println("CHILD_REMOVED:" + event.getData());
				break;

			default:
				break;
			}
		}

	}

}
