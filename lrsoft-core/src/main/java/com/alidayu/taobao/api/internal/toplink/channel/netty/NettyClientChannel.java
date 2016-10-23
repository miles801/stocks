package com.alidayu.taobao.api.internal.toplink.channel.netty;

import com.alidayu.taobao.api.internal.toplink.channel.ClientChannel;
import org.jboss.netty.channel.Channel;


public interface NettyClientChannel extends ClientChannel {
    public void setChannel(Channel channel);
}
