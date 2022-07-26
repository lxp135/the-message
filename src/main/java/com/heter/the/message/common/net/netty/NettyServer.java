package com.heter.the.message.common.net.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;

@Service
public class NettyServer {

	private final Logger logger = LoggerFactory.getLogger(NettyServer.class);

	//bossGroup用于服务端接受客户端的连接
	private final EventLoopGroup bossGroup = new NioEventLoopGroup(4);
	//workerGroup进行SocketChannel的网络读写
	private final EventLoopGroup workerGroup = new NioEventLoopGroup();
	private Channel channel;

	@Value("${netty.maxReceiveBytes}")
	private int maxReceiveBytes;

	@Value("${netty.serverPort}")
	private int serverPort;

	public void start() throws Exception {
		logger.info("netty socket服务已启动，正在监听用户的请求@port:" + serverPort + "......");
		try {
			//Netty用于启动NIO服务端的辅助启动类，目的是降低服务端的开发复杂度
			ServerBootstrap b = new ServerBootstrap();
			//将两个NIO线程组当作入参传递到ServerBootstrap
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					//配置NioServerSocketChannel的TCP参数，此处将它的backlog设置为1024
					.option(ChannelOption.SO_BACKLOG, 1024)
					//绑定I/O事件的处理类ChildChannelHandler，它的作用类似于Reactor模式中的Handler类，主要用于处理网络I/O事件，例如记录日志、对消息进行编解码等
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							//采用分割符解决半包黏包
							ch.pipeline().addLast(new LineBasedFrameDecoder(maxReceiveBytes));
							//字符串的编解码器
							ch.pipeline().addLast(new StringDecoder());
							ch.pipeline().addLast(new StringEncoder());
							//添加自己的消息处理器
							ch.pipeline().addLast(new NettyCmdHandler());
						}
					});
			//调用bind方法绑定监听端口，随后，调用它的同步阻塞方法sync等待绑定操作完成。
			ChannelFuture  f = b.bind(new InetSocketAddress(serverPort)).sync();
			//完成之后Netty会返回一个ChannelFuture，它的功能类似于JDK的java.util.concurrent.Future，主要用于异步操作的通知回调
			channel = f.channel();

		} catch (Exception e) {
			logger.error("Netty服务启动失败", e);
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();

			throw e;
		}
	}

	// 销毁方法
	public void destroy() {
		if (channel==null){return;}
		channel.close();
		workerGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();
	}


}
