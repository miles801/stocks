package com.alidayu.taobao.api.internal.toplink.netcat.netty;

import com.alidayu.taobao.api.internal.toplink.LoggerFactory;
import com.alidayu.taobao.api.internal.toplink.channel.ChannelHandler;
import com.alidayu.taobao.api.internal.toplink.channel.tcp.TcpServerChannel;
import com.alidayu.taobao.api.internal.toplink.logging.LogUtil;
import com.alidayu.taobao.api.internal.toplink.netcat.CommandProcessor;
import com.alidayu.taobao.api.internal.toplink.netcat.NetCatCommandServerChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.handler.codec.string.StringDecoder;

public class NettyNetCatCommandServerChannel extends TcpServerChannel {
	public NettyNetCatCommandServerChannel(int port) {
		this(LogUtil.getLoggerFactory(new Object()), port);
	}

	public NettyNetCatCommandServerChannel(LoggerFactory factory, int port) {
		super(factory, port);
		this.channelHandler = new NetCatCommandServerChannelHandler();
	}

	@Override
	public void setChannelHandler(ChannelHandler channelHandler) {
		return;
	}

	@Override
	protected void prepareCodec(ChannelPipeline pipeline) {
		pipeline.addLast("decoder", new StringDecoder());
	}

	public void addProcessor(CommandProcessor processor) {
		((NetCatCommandServerChannelHandler) this.channelHandler).addProcessor(processor);
	}
}
