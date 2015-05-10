package com.splendidvenus.xml;

import java.util.Collections;
import java.util.Map;

public class XMLAttribute extends XML {

	private String	qname;
	private String	value;

	public XMLAttribute(String qname, String value) {
		super(NodeType.ATTRITUBE);
		this.value = value;
		this.qname = qname;
	}

	@Override
	public Map<String, XML> asMap(String key) {
		if (key == null || key.length() == 0 || key.equals("@")) throw new NullPointerException("key is null or empty");
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

	@Override
	public boolean isLeafNode() {
		return true;
	}

	@Override
	public String getNodeName() {
		return qname;
	}
}
