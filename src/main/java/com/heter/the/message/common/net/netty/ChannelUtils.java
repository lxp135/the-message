package com.heter.the.message.common.net.netty;

import com.heter.the.message.common.net.IdSession;
import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;

public class ChannelUtils {
	
public static AttributeKey<IdSession> SESSION_KEY = AttributeKey.valueOf("session");
	
	/**
	 * 添加新的会话
	 * @param channel
	 * @param session
	 * @return
	 */
	public static boolean addChannelSession(Channel channel, IdSession session) {
		Attribute<IdSession> sessionAttr = channel.attr(SESSION_KEY);
		return sessionAttr.compareAndSet(null, session);
	}
	
	public static IdSession getSessionBy(Channel channel) {
		Attribute<IdSession> sessionAttr = channel.attr(SESSION_KEY);
		return sessionAttr.get() ;
	}
	
	public static String getIp(Channel channel) {
		return ((InetSocketAddress)channel.remoteAddress()).getAddress().toString().substring(1);
	}
}
