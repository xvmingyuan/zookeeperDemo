package com.study._6balance.server;
/**
 * 负载均衡更新器
 * @author xmy
 * @time：2018年9月20日 下午10:08:44
 */
public interface BalanceUpdateProvider {
	public boolean addBalance(Integer step);
	public boolean reduceBalance(Integer step);
}
