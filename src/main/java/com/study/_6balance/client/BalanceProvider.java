package com.study._6balance.client;
//负载的算法接口
public interface BalanceProvider<T> {
	// 获取经过算法算出的负载抽象对象
	public T getBalanceItem() ;
		
}
