package com.alidayu.taobao.api.internal.toplink.remoting;

import com.alidayu.taobao.api.internal.toplink.LoggerFactory;
import com.alidayu.taobao.api.internal.toplink.channel.tcp.TcpServerChannel;
import org.jboss.netty.channel.ChannelPipeline;

public class NettyRemotingTcpServerChannel extends TcpServerChannel {

	public NettyRemotingTcpServerChannel(int port) {
		super(port);
	}
	
	public NettyRemotingTcpServerChannel(LoggerFactory factory, int port) {
		super(factory, port);
	}

	@Override
	protected void prepareCodec(ChannelPipeline pipeline) {
		pipeline.addLast("decoder", new NettyRemotingDecoder());
	}
}
