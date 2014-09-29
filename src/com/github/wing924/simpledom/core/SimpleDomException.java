package com.github.wing924.simpledom.core;

public class SimpleDomException extends RuntimeException {

	private static final long	serialVersionUID	= -2576978596630197118L;

	public SimpleDomException() {
		super();
	}

	public SimpleDomException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public SimpleDomException(String detailMessage) {
		super(detailMessage);
	}

	public SimpleDomException(Throwable throwable) {
		super(throwable);
	}
}
