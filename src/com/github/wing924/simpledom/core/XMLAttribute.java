package com.github.wing924.simpledom.core;

import java.util.Collections;
import java.util.Map;

public class XMLAttribute extends XML {

	private String	value;

	public XMLAttribute(String qname, String value) {
		super(XMLNodeType.ATTRITUBE, qname);
		this.value = value;
	}

	@Override
	public Map<String, XML> asMap(String key) {
		if (key == null || key.length() == 0 || key.equals("@"))
			throw new NullPointerException("key is null or empty");
		return Collections.emptyMap();
	}

	@Override
	public String toString() {
		return value.toString();
	}

	@Override
	protected String getValue() {
		return value;
	}
}
