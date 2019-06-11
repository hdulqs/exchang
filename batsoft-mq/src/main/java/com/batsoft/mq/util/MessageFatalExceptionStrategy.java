package com.batsoft.mq.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.listener.FatalExceptionStrategy;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.messaging.handler.annotation.support.MethodArgumentTypeMismatchException;

public class MessageFatalExceptionStrategy implements FatalExceptionStrategy {

	private static final Logger logger = LoggerFactory.getLogger(MessageFatalExceptionStrategy.class);

	@Override
	public boolean isFatal(Throwable t) {
		logger.debug("isFatal");
		if (t instanceof ListenerExecutionFailedException && causeIsFatal(t.getCause())) {
			logger.debug("通知管理员程序存在bug，导致"+t.getMessage()+"，使得消费端瘫痪");
			return true;
		}
		return false;
	}
	
	public static boolean causeIsFatal(Throwable cause) {
		return cause instanceof MessageConversionException
				|| cause instanceof org.springframework.messaging.converter.MessageConversionException
				|| cause instanceof MethodArgumentNotValidException
				|| cause instanceof MethodArgumentTypeMismatchException
				|| cause instanceof NullPointerException;
	}
	
}
