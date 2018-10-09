package com.study._7lock;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.ZkClient;

/**
 * 简单的 互斥锁
 * 
 * @author xmy
 * @time：2018年9月24日 下午6:00:55
 */
public class LockIImpl extends BaseLock implements LockI {
	/** 锁名称前缀 */
	private static final String LOCK_NAME = "lock-";
	
	/** Zookeeper中locker节点的路径，如：/locker */
	private final String basePath;

	/** 获取锁以后自己创建的那个顺序节点的路径 */
	private String ourLockPath;
	

	public LockIImpl(ZkClient client, String basePath) {
		super(client, basePath, LOCK_NAME);
		this.basePath = basePath;
		
	}

	@Override
	public void getLock() throws Exception {
		// -1 表示永不超时
		ourLockPath = tryGetLock(-1, null);
		if(ourLockPath == null) {
			throw new IOException("连接丢失!在路径:'" + basePath + "'下不能获取锁!");
		}

	}

	@Override
	public boolean getLock(long timeOut, TimeUnit timeUnit) throws Exception {
		ourLockPath = tryGetLock(timeOut, timeUnit);
		return ourLockPath !=null;

	}

	@Override
	public void releaseLock() throws Exception {
		releaseLock(ourLockPath);

	}

}
