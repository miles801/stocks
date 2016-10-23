package com.alidayu.taobao.api.internal.toplink.endpoint;

import com.alidayu.taobao.api.internal.toplink.ResetableTimer;
import com.alidayu.taobao.api.internal.toplink.channel.ChannelHandler;
import com.alidayu.taobao.api.internal.toplink.channel.ClientChannel;

import java.net.URI;

public class ClientChannelWrapper extends ChannelSenderWrapper implements ClientChannel {
	private ClientChannel clientChannel;

	public ClientChannelWrapper(ClientChannel clientChannel, int protocolVersion) {
		super(clientChannel, protocolVersion);
		this.clientChannel = clientChannel;
	}

	public boolean isConnected() {
		return this.clientChannel.isConnected();
	}

	public ChannelHandler getChannelHandler() {
		return this.clientChannel.getChannelHandler();
	}

	public void setChannelHandler(ChannelHandler handler) {
		this.clientChannel.setChannelHandler(handler);
	}

	public void setUri(URI uri) {
		this.clientChannel.setUri(uri);
	}

	public URI getUri() {
		return this.clientChannel.getUri();
	}

	public void setHeartbeatTimer(ResetableTimer timer) {
		this.clientChannel.setHeartbeatTimer(timer);
	}
}
