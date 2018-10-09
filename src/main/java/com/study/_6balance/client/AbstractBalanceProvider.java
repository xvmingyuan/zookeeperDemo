package com.study._6balance.client;

import java.util.List;

/**
 * 抽象负载均衡算法
 * 
 * @author xmy
 * @time：2018年9月19日 下午10:40:27
 * @param <T>
 */
public abstract class AbstractBalanceProvider<T> implements BalanceProvider<T> {
	// 负载均衡算法
	protected abstract T balanceAlgorithm(List<T> item);

	// 获取负载服务器
	protected abstract List<T> getBalanceItems();
	
	//实现接口BalanceProvider的getBalanceItem方法()
	public T getBalanceItem() {
		// 调用抽象负载均衡算法(获取当前抽象负载服务器) 返回一个负载轻的抽象对象
		return balanceAlgorithm(getBalanceItems());
	}
}
