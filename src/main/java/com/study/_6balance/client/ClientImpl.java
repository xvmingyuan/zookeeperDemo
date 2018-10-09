package com.study._6balance.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.study._6balance.server.ServerData;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 客户端实现类
 * 
 * @author xmy
 * @time：2018年9月20日 下午10:02:13
 */
public class ClientImpl implements Client {
	private final BalanceProvider<ServerData> provider;
	private EventLoopGroup group = null;
	private Channel channel = null;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	public ClientImpl(BalanceProvider<ServerData> provider) {
		this.provider = provider;
	}

	public BalanceProvider<ServerData> getProvider() {
		return provider;
	}

	@Override
	public void connect() throws Exception {
		try {
			// 调用负载均衡后服务器配置
			ServerData serverData = provider.getBalanceItem();
			System.out.println("负载均衡服务器为:"+serverData);

			group = new NioEventLoopGroup();
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();
					p.addLast(new ClientHandler());
				}
			});
			ChannelFuture f = b.connect(serverData.getHost(),serverData.getPort()).syncUninterruptibly();
			channel = f.channel();
			System.out.println("client : started success!");
		} catch (Exception e) {
			System.out.println("连接异常:"+e.getMessage());    
		}
	}

	@Override
	public void disConnect() throws Exception {
		try {
			if (channel != null) {
				channel.close().syncUninterruptibly();
			}
			group.shutdownGracefully();
			group = null;
			System.out.println("client : disconnected!");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}

	}

}
