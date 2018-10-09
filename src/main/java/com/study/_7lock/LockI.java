package com.study._7lock;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁接口
 * @author xmy
 * @time：2018年9月24日 下午6:01:11
 */
public interface LockI {
	// 获取锁,如果没有得到就等待
	void getLock() throws Exception;
	
	//获取锁,直到超时
	boolean getLock(long timeOut,TimeUnit unit) throws Exception;
	
	// release lock
	void releaseLock() throws Exception;
	
	
	
	
}
