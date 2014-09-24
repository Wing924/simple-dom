package com.github.wing924.simpledom.utils;

public class Assert {

	public static <T> void equals(T expect, T actual) {
		if (!expect.equals(actual)) {
			throw new AssertException(String.format("Assertion failured! Expect %s, but was %s.", expect, actual));
		}
	}

	public static <T> void equals(T expect, T actual, String message) {
		if (!expect.equals(actual)) {
			throw new AssertException(String.format("Assertion failured! Expect %s, but was %s. Message: %s", expect,
					actual, message));
		}
	}

	public static class AssertException extends RuntimeException {

		private static final long	serialVersionUID	= 8280847668082073253L;

		AssertException(String detailMessage, Throwable throwable) {
			super(detailMessage, throwable);
		}

		AssertException(String detailMessage) {
			super(detailMessage);
		}
	}
}
