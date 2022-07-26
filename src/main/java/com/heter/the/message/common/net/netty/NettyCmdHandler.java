package com.heter.the.message.common.net.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyCmdHandler extends ChannelInboundHandlerAdapter {


    private final Logger logger = LoggerFactory.getLogger(NettyCmdHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof String) {
            String strMsg = (String) msg;
            logger.info("服务端收到消息: " + msg);


        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("连接建立.....");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("连接断开");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("连接异常: \r\n" + cause);
    }


}
