package com.alidayu.taobao.api.internal.toplink.remoting;

import com.alidayu.taobao.api.internal.toplink.LoggerFactory;
import com.alidayu.taobao.api.internal.toplink.channel.ChannelException;
import com.alidayu.taobao.api.internal.toplink.channel.ClientChannel;
import com.alidayu.taobao.api.internal.toplink.channel.ClientChannelSharedSelector;
import com.alidayu.taobao.api.internal.toplink.channel.tcp.TcpClient;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;

import java.net.URI;

public class NettyRemotingClientChannelSharedSelector extends ClientChannelSharedSelector {
	@Override
	protected ClientChannel connect(LoggerFactory loggerFactory, URI uri, int timeout) throws ChannelException {
		if (uri.getScheme().equalsIgnoreCase("tcp") || 
				uri.getScheme().equalsIgnoreCase("ssl")) {
			ChannelPipeline pipeline = Channels.pipeline();
			pipeline.addLast("decoder", new NettyRemotingDecoder());
			return TcpClient.connect(loggerFactory, uri, timeout, pipeline);
		}
		return super.connect(loggerFactory, uri, timeout);
	}
}
