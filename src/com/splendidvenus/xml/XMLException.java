package com.splendidvenus.xml;

public class XMLException extends RuntimeException {

	private static final long	serialVersionUID	= -2576978596630197118L;

	public XMLException() {
		super();
	}

	public XMLException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public XMLException(String detailMessage) {
		super(detailMessage);
	}

	public XMLException(Throwable throwable) {
		super(throwable);
	}
}
