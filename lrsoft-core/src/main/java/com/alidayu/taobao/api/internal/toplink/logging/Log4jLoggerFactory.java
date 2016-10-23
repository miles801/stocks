package com.alidayu.taobao.api.internal.toplink.logging;

import com.alidayu.taobao.api.internal.toplink.Logger;
import com.alidayu.taobao.api.internal.toplink.LoggerFactory;
import org.apache.log4j.LogManager;

public class Log4jLoggerFactory implements LoggerFactory {
	public Logger create(String type) {
		return new Log4jLogger(LogManager.getLogger(type));
	}
	
	public Logger create(Class<?> type) {
		return new Log4jLogger(LogManager.getLogger(type));
	}
	
	public Logger create(Object object) {
		return new Log4jLogger(LogManager.getLogger(object.getClass()));
	}
}
