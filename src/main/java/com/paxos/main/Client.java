package com.paxos.main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.paxos.doer.Acceptor;
import com.paxos.doer.Proposer;

/**
 * 客户端(用来启动提议事件)
 * Paxos算法一致性实现
 * @author xmy
 * @time：2018年8月21日 上午9:53:54/
 */
public class Client {
	// 竞选者数量(注:必须是大于3的奇数个)
	private static final int NUM_OF_PROPOSER = 5;
	// 投票者数量(注:必须是大于3的奇数个)
	private static final int NUM_OF_ACCEPTOR = 3;
	//多线程模拟竞选者竞选场景条件
	public static CountDownLatch latch = new CountDownLatch(NUM_OF_PROPOSER);
	
	public static void main(String[] args) {
		List<Acceptor> acceptors = new ArrayList<Acceptor>();
		for (int i=0;i<NUM_OF_ACCEPTOR;i++) {
			acceptors.add(new Acceptor(i));
		}
		ExecutorService es = Executors.newCachedThreadPool();
		for(int i=0;i<NUM_OF_PROPOSER;i++) {
			Proposer proposer = new Proposer(i, "name#"+i+"Proposer", NUM_OF_PROPOSER, acceptors);
			es.submit(proposer);
		}
		es.shutdown();
		
	}
	
}
