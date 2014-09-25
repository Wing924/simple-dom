package com.github.wing924.simpledom.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class XMLElement extends XML {

	private Map<String, XML>	attributes;
	private Map<String, XML>	elements;
	private String				nodeValue;

	public XMLElement(String qname) {
		super(XMLNodeType.ELEMENT, qname);
	}

	public XMLElement(String qname, String value) {
		super(XMLNodeType.ELEMENT, qname);
		nodeValue = value;
	}

	@Override
	public boolean isLeafNode() {
		return nodeValue != null;
	}

	@Override
	public XML get(String qname) {
		XML xml = getChild(qname);
		if (xml == null) throw new XMLException("no such element or attritube: " + qname);
		return xml;
	}

	@Override
	public XML opt(String qname) {
		XML xml = getChild(qname);
		if (xml == null) return NULL_NODE;
		return xml;
	}

	@Override
	public boolean has(String qname) {
		return getChild(qname) != null;
	}

	private XML getChild(String qname) {
		if (qname == null) throw new NullPointerException("qname nust not be null or empty String.");
		if (qname.length() == 0) throw new IllegalArgumentException("qname must not be empty String.");
		if (qname.charAt(0) == '@') {
			if (qname.length() <= 1) throw new IllegalArgumentException("qname must not be '@'.");
			return attributes == null ? null : attributes.get(qname.substring(1));
		}
		return elements == null ? null : elements.get(qname);
	}

	@Override
	public Map<String, XML> asMap(String key) {
		return Collections.<String, XML> singletonMap(opt(key).asString(""), this);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<" + getNodeName());
		if (attributes != null) {
			for (Map.Entry<String, XML> entry : attributes.entrySet()) {
				sb.append(" " + entry.getKey() + "=\"" + entry.getValue().toString() + "\"");
			}
		}
		sb.append(">");
		if (elements != null) {
			for (Map.Entry<String, XML> entry : elements.entrySet()) {
				sb.append(entry.getValue().toString());
			}
		} else {
			sb.append(nodeValue);
		}
		sb.append("</" + getNodeName() + ">");
		return sb.toString();
	}

	@Override
	public Iterator<XML> iterator() {
		return Collections.<XML> singletonList(this).iterator();
	}

	@Override
	public List<XML> attritubes() {
		if (attributes == null || attributes.isEmpty()) return super.attritubes();
		List<XML> list = new ArrayList<XML>();
		for (XML v : attributes.values()) {
			list.add(v);
		}
		return list;
	}

	@Override
	public List<XML> children() {
		if (elements == null || elements.isEmpty()) return super.children();
		List<XML> list = new ArrayList<XML>();
		for (XML v : elements.values()) {
			switch (v.getNodeType()) {
				case ELEMENT:
					list.add(v);
					break;
				case ELEMENT_LIST:
					for (XML x : v) {
						list.add(x);
					}
					break;
				default:
					break;
			}
		}
		return list;
	}

	@Override
	protected String getValue() {
		return nodeValue;
	}

	// internal use

	void appendChild(XML xml) {
		if (xml.isAttribute()) {
			if (attributes == null) attributes = new LinkedHashMap<String, XML>();
			attributes.put(xml.getNodeName(), xml);
		} else {
			if (nodeValue != null) throw new IllegalStateException("only leaf node can contain text");
			if (elements == null) elements = new LinkedHashMap<String, XML>();
			String qname = xml.getNodeName();
			XML child = elements.get(qname);
			if (child == null) {
				elements.put(qname, xml);
			} else if (child.getNodeType() == XMLNodeType.ELEMENT_LIST) {
				((XMLElementList) child).appendToList(xml);
			} else {
				XMLElementList xmllist = new XMLElementList(qname);
				xmllist.appendToList(child);
				xmllist.appendToList(xml);
				elements.put(qname, xmllist);
			}
		}
	}

	void setNodeValue(String string) {
		if (elements != null) throw new IllegalStateException("only leaf node can contain text");
		nodeValue = string;
	}
}
