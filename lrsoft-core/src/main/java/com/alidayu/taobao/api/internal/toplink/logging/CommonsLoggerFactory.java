package com.alidayu.taobao.api.internal.toplink.logging;

import com.alidayu.taobao.api.internal.toplink.Logger;
import com.alidayu.taobao.api.internal.toplink.LoggerFactory;
import org.apache.commons.logging.impl.LogFactoryImpl;

public class CommonsLoggerFactory implements LoggerFactory {
	public Logger create(String type) {
		return new CommonsLogger(LogFactoryImpl.getLog(type));
	}

	public Logger create(Class<?> type) {
		return new CommonsLogger(LogFactoryImpl.getLog(type));
	}

	public Logger create(Object object) {
		return new CommonsLogger(LogFactoryImpl.getLog(object.getClass()));
	}
}
