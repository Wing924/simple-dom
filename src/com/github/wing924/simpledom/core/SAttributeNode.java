package com.github.wing924.simpledom.core;

import java.util.Collections;
import java.util.Map;

public class SAttributeNode extends SNode {

	private String	qname;
	private String	value;

	public SAttributeNode(String qname, String value) {
		super(NodeType.ATTRITUBE);
		this.value = value;
		this.qname = qname;
	}

	@Override
	public Map<String, SNode> asMap(String key) {
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
