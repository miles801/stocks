package com.alidayu.taobao.api.internal.toplink.endpoint;

import com.alidayu.taobao.api.internal.toplink.endpoint.protocol.DefaultMessageDecoderFactory;
import com.alidayu.taobao.api.internal.toplink.endpoint.protocol.DefaultMessageEncoderFactory;
import com.alidayu.taobao.api.internal.toplink.endpoint.protocol.MessageDecoderFactory;
import com.alidayu.taobao.api.internal.toplink.endpoint.protocol.MessageEncoderFactory;

import java.nio.ByteBuffer;

// simple protocol impl
// care about Endian
// https://github.com/wsky/RemotingProtocolParser/issues/3
public class MessageIO {
	public interface MessageEncoder {
		public void writeMessage(ByteBuffer buffer, Message message);
	}

	public interface MessageDecoder {
		public Message readMessage(ByteBuffer buffer);
	}

	// TODO codec will rewrite in v2.0, current design just for compatible
	public static MessageEncoderFactory encoderFactory = new DefaultMessageEncoderFactory();
	public static MessageDecoderFactory decoderFactory = new DefaultMessageDecoderFactory();

	public static Message readMessage(ByteBuffer buffer) {
		return decoderFactory.get(buffer).readMessage(buffer);
	}

	public static void writeMessage(ByteBuffer buffer, Message message) {
		encoderFactory.get(message).writeMessage(buffer, message);
	}
}