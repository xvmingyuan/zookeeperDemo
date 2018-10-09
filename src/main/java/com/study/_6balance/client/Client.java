package com.study._6balance.client;
/**
 * 客户端接口
 * @author xmy
 * @time：2018年9月19日 下午10:31:10
 */

public interface Client {
	//	与服务器连接
	public void connect() throws Exception;
	//  与服务器断开连接
	public void disConnect() throws Exception;
}
