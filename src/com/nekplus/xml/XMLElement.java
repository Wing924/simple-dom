package com.nekplus.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class XMLElement extends XML {

	private Map<String, XML> attributes;
	private Map<String, XML> elements;
	private List<XML> elementsOrder;
	private String qname;
	private String nodeValue;

	public XMLElement(String qname) {
		super(NodeType.ELEMENT);
		this.qname = qname;
	}

	public XMLElement(String qname, String value) {
		super(NodeType.ELEMENT);
		nodeValue = value;
		this.qname = qname;
	}

	@Override
	public String getNodeName() {
		return qname;
	}

	@Override
	public boolean isLeafNode() {
		return nodeValue != null;
	}

	@Override
	public XML getAttribute(String qname) {
		if (attributes == null) return NULL;
		XML node = attributes.get(qname);
		return node == null ? NULL : node;
	}

	@Override
	public XML getElement(String qname) {
		if (elements == null) return NULL;
		XML node = elements.get(qname);
		return node == null ? NULL : node;
	}

	@Override
	public boolean has(String qname) {
		return get(qname) != NULL;
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
		List<XML> list = new ArrayList<XML>(elementsOrder);
		return list;
	}

	@Override
	protected String getValue() {
		return nodeValue;
	}

	// internal use

	void appendChild(XML xml) {
		if (xml.isAttribute()) {
			if (attributes == null) attributes = new TreeMap<String, XML>();
			attributes.put(xml.getNodeName(), xml);
		} else {
			if (nodeValue != null) throw new IllegalStateException("only leaf node can contain text");
			if (elements == null) {
				elements = new TreeMap<String, XML>();
				elementsOrder = new ArrayList<XML>();
			}
			elementsOrder.add(xml);
			String qname = xml.getNodeName();
			XML child = elements.get(qname);
			if (child == null) {
				elements.put(qname, xml);
			} else if (child.getNodeType() == NodeType.ELEMENT_LIST) {
				((XMLList) child).appendToList(xml);
			} else {
				XMLList xmllist = new XMLList();
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
