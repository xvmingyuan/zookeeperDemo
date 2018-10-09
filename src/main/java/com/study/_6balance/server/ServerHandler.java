package com.study._6balance.server;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 处理与客户端之间的连接 客户端连接断开等触发此类
 * 
 * @author xmy
 * @time：2018年9月20日 下午10:38:02
 */
public class ServerHandler extends ChannelHandlerAdapter {
	private final BalanceUpdateProvider balanceUpdater;
	/** 负载均衡累加数值 */
	private static final Integer BALANCE_STEP = 1;

	public ServerHandler(BalanceUpdateProvider balanceUpdater) {
		this.balanceUpdater = balanceUpdater;
	}

	public BalanceUpdateProvider getBalanceUpdater() {
		return balanceUpdater;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("one client connect...");
		// 客户端做了同步调用处理,调用此方法
		// balanceUpdater.addBalance(BALANCE_STEP);

		// 客户端属于争抢资源类型,调用此方法
		synchronized (this) {
			balanceUpdater.addBalance(BALANCE_STEP);
		}

	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("one client disconnect...");
		// 客户端做了同步调用处理,调用此方法
//		balanceUpdater.reduceBalance(BALANCE_STEP);
		
		// 客户端属于争抢资源类型,调用此方法
		synchronized (ServerHandler.class) {
			balanceUpdater.reduceBalance(BALANCE_STEP);
		}

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
