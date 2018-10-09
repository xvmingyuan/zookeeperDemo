package com.study._6balance.server;

/**
 * 服务端启动时的注册过程
 * 
 * @author xmy
 * @time：2018年9月20日 下午10:12:57
 */
public interface RegistProvider {

	public void regist(Object context) throws Exception;

	public void unRegist(Object context) throws Exception;

}
