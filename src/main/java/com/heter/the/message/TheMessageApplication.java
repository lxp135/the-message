package com.heter.the.message;

import com.heter.the.message.common.net.netty.NettyServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

/**
 * 风声桌游服务端启动类
 */
@SpringBootApplication
public class TheMessageApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(TheMessageApplication.class, args);
	}

	@Resource
	private NettyServer nettyServer;

	@Override
	public void run(String... args) throws Exception {

		nettyServer.start();

		Runtime.getRuntime().addShutdownHook(new Thread(()->{
			// jvm钩子，执行netty销毁方法
			nettyServer.destroy();
		}));
	}
}