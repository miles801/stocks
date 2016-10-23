package com.alidayu.taobao.api.internal.toplink.channel.websocket;

import com.alidayu.taobao.api.internal.toplink.channel.ServerChannelSender;
import org.jboss.netty.channel.Channel;

public class WebSocketServerChannelSender extends WebSocketChannelSender implements ServerChannelSender {
	public WebSocketServerChannelSender(Channel channel) {
		super(channel);
	}

	public boolean isOpen() {
		return this.channel.isOpen();
	}
}
