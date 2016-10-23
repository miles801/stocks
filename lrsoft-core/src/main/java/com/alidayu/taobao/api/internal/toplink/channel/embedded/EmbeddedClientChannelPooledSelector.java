package com.alidayu.taobao.api.internal.toplink.channel.embedded;

import com.alidayu.taobao.api.internal.toplink.LoggerFactory;
import com.alidayu.taobao.api.internal.toplink.channel.ChannelException;
import com.alidayu.taobao.api.internal.toplink.channel.ClientChannel;
import com.alidayu.taobao.api.internal.toplink.channel.ClientChannelPooledSelector;

import java.net.URI;

public class EmbeddedClientChannelPooledSelector extends ClientChannelPooledSelector {
    public EmbeddedClientChannelPooledSelector() {
        super();
    }

    public EmbeddedClientChannelPooledSelector(LoggerFactory loggerFactory) {
        super(loggerFactory);
    }

    protected ChannelPool createChannelPool(LoggerFactory loggerFactory, URI uri, int timeout) {
        return new ChannelPool(loggerFactory, uri, timeout);
    }

    public class EmbeddedChannelPool extends ChannelPool {
        public EmbeddedChannelPool(LoggerFactory loggerFactory, URI uri, int timeout) {
            super(loggerFactory, uri, timeout);
        }

        @Override
        public ClientChannel create() throws ChannelException {
            return uri.getScheme().equalsIgnoreCase("ws") ||
                    uri.getScheme().equalsIgnoreCase("wss") ?
                    EmbeddedWebSocketClient.connect(this.loggerFactory, this.uri, this.timeout) :
                    super.create();
        }
    }
}