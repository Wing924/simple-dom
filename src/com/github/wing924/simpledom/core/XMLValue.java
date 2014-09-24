package com.github.wing924.simpledom.core;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

class XMLValue extends XML {

	private String	value;

	public XMLValue(XMLNodeType nodeType, String qname, String value) {
		super(nodeType, qname);
		this.value = value;
	}

	@Override
	public XML get(String qname) {
		if (getNodeType() == XMLNodeType.ELEMENT) {
			throw new XMLException("no such elemets or attritubes <" + qname + ">");
		}
		return super.get(qname);
	}

	@Override
	public Map<String, XML> asMap(String key) {
		if (key == null || key.length() == 0 || key.equals("@")) throw new NullPointerException("key is null or empty");
		if (getNodeType() == XMLNodeType.ELEMENT) return Collections.<String, XML> singletonMap("", this);
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
	public Iterator<XML> iterator() {
		if (getNodeType() == XMLNodeType.ELEMENT) return Collections.<XML> singletonList(this).iterator();
		return super.iterator();
	}
}