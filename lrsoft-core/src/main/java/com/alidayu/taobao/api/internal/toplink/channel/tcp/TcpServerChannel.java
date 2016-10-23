package com.alidayu.taobao.api.internal.toplink.channel.tcp;

import com.alidayu.taobao.api.internal.toplink.DefaultLoggerFactory;
import com.alidayu.taobao.api.internal.toplink.LoggerFactory;
import com.alidayu.taobao.api.internal.toplink.channel.netty.NettyServerChannel;
import org.jboss.netty.channel.ChannelPipeline;

public abstract class TcpServerChannel extends NettyServerChannel {

	public TcpServerChannel(int port) {
		this(DefaultLoggerFactory.getDefault(), port);
	}

	public TcpServerChannel(LoggerFactory factory, int port) {
		super(factory, port);
	}

	protected void preparePipeline(ChannelPipeline pipeline) {
		this.prepareCodec(pipeline);
		pipeline.addLast("handler", this.createHandler());
	}

	protected abstract void prepareCodec(ChannelPipeline pipeline);

	protected TcpServerUpstreamHandler createHandler() {
		return new TcpServerUpstreamHandler(
				this.loggerFactory,
				this.channelHandler,
				this.allChannels);
	}
}