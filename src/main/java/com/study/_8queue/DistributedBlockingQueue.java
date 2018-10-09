package com.study._8queue;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

/**
 * 简单的分布式队列(阻塞) 比较合理的做法
 * 
 * @author xmy
 * @time：2018年10月9日 下午8:24:50
 */
public class DistributedBlockingQueue<T> extends DistributedSimpleQueue<T> {

	public DistributedBlockingQueue(ZkClient zkClient, String root) {
		super(zkClient, root);
	}

	/**
	 * 一直阻塞 一有数据就消费然后继续阻塞(获取数据)
	 *
	 * @return
	 * @throws Exception
	 */
	@Override
	public T pull() throws Exception {
		while (true) {

			final CountDownLatch latch = new CountDownLatch(1);
			IZkChildListener childListener = new IZkChildListener() {

				@Override
				public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
					latch.countDown();
				}
			};
			zkClient.subscribeChildChanges(root, childListener);
			try {
				T node = super.pull();
				if (node!=null) {
					return node;
				}else {
					latch.await();
				}

			} finally {
				zkClient.unsubscribeChildChanges(root, childListener);
			}

		}
	}
}
