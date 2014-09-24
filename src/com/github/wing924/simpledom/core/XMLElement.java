package com.github.wing924.simpledom.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class XMLElement extends XML {

	private Map<String, XML>	children;
	private String				nodeValue;

	public XMLElement(String qname) {
		super(XMLNodeType.ELEMENT, qname);
	}

	public XMLElement(String qname, String value) {
		super(XMLNodeType.ELEMENT, qname);
		nodeValue = value;
	}

	@Override
	public XML get(String qname) {
		if (children == null) throw new XMLException("no such elemets or attritubes <" + qname + ">");
		XML element = children.get(qname);
		if (element == null) throw new XMLException("no such elemets or attritubes <" + qname + ">");
		return element;
	}

	@Override
	public XML opt(String qname) {
		if (children == null) return XML.NULL_NODE;
		XML xml = children.get(qname);
		return xml == null ? XML.NULL_NODE : xml;
	}

	@Override
	public Map<String, XML> asMap(String key) {
		if (key == null || key.length() == 0 || key.equals("@"))
			throw new NullPointerException("key is null or empty");

		return Collections.<String, XML> singletonMap(opt(key).optString(), this);
	}

	void appendChild(XML xml) {
		if (children == null) children = new LinkedHashMap<String, XML>();
		String qname = xml.getNodeName();
		XML child = children.get(qname);
		if (child == null) {
			children.put(qname, xml);
		} else if (child.getNodeType() == XMLNodeType.ELEMENT_LIST) {
			((XMLElementList) child).appendToList(xml);
		} else {
			XMLElementList xmllist = new XMLElementList(qname);
			xmllist.appendToList(child);
			xmllist.appendToList(xml);
			children.put(qname, xmllist);
		}
	}

	void setNodeValue(String string) {
		nodeValue = string;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<" + getNodeName());
		if (children != null) {
			for (Map.Entry<String, XML> entry : children.entrySet()) {
				if (entry.getKey().startsWith("@")) {
					sb.append(" " + entry.getKey().substring(1) + "=\"" + entry.getValue().toString() + "\"");
				}
			}
		}
		sb.append(">");
		for (Map.Entry<String, XML> entry : children.entrySet()) {
			if (!entry.getKey().startsWith("@")) {
				sb.append(entry.getValue().toString());
			}
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
		if (children == null || children.isEmpty()) return super.attritubes();
		List<XML> list = new ArrayList<XML>();
		for (XML v : children.values()) {
			if (v.getNodeType() == XMLNodeType.ATTRITUBE) list.add(v);
		}
		return list;
	}

	@Override
	public List<XML> children() {
		if (children == null || children.isEmpty()) return super.children();
		List<XML> list = new ArrayList<XML>();
		for (XML v : children.values()) {
			if (v.getNodeType() == XMLNodeType.ELEMENT) list.add(v);
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
}
