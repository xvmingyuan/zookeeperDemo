package com.study._3curatorapi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.RetryUntilElapsed;

public class _7_2CheckExistsASync {
	public static void main(String[] args) throws Exception {
		// 异步调用每次都是创建一个线程，如果系统执行的异步调用比较多，会创建比较多的线程，这里我们需要使用线程池
		ExecutorService es = Executors.newFixedThreadPool(5);

		RetryPolicy retryPolicy = new RetryUntilElapsed(5000, 1000);
		// Fluent风格
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString("it01:2181").sessionTimeoutMs(5000)
				.connectionTimeoutMs(5000).retryPolicy(retryPolicy).build();

		client.start();
		client.checkExists().inBackground(new IBackgroundCallback(), "Context Value", es).forPath("/node3");
		Thread.sleep(Integer.MAX_VALUE);

	}

	private static class IBackgroundCallback implements BackgroundCallback {

		@Override
		public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
			System.out.println("stat = " + event.getStat());
			System.out.println("ResultCode = " + event.getResultCode());
			System.out.println("Context = " + event.getContext());
		}

	}

}
