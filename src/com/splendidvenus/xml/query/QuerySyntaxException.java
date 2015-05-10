package com.splendidvenus.xml.query;

import com.splendidvenus.xml.XMLException;

public class QuerySyntaxException extends XMLException {

	public QuerySyntaxException() {
	}

	public QuerySyntaxException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public QuerySyntaxException(String detailMessage) {
		super(detailMessage);
	}

	public QuerySyntaxException(Throwable throwable) {
		super(throwable);
	}
}
