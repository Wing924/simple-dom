package com.github.wing924.simpledom.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class SElementNode extends SNode {

	private Map<String, SNode>	attributes;
	private Map<String, SNode>	elements;
	private List<SNode>			elementsOrder;
	private String				qname;
	private String				nodeValue;

	public SElementNode(String qname) {
		super(NodeType.ELEMENT);
		this.qname = qname;
	}

	public SElementNode(String qname, String value) {
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
	public SNode get(String qname) {
		SNode xml = getChild(qname);
		if (xml == null) throw new SimpleDomException("no such element or attritube: " + qname);
		return xml;
	}

	@Override
	public SNode opt(String qname) {
		SNode xml = getChild(qname);
		if (xml == null) return NULL_NODE;
		return xml;
	}

	@Override
	public boolean has(String qname) {
		return getChild(qname) != null;
	}

	private SNode getChild(String qname) {
		if (qname == null) throw new NullPointerException("qname nust not be null or empty String.");
		if (qname.length() == 0) throw new IllegalArgumentException("qname must not be empty String.");
		if (qname.charAt(0) == '@') {
			if (qname.length() <= 1) throw new IllegalArgumentException("qname must not be '@'.");
			return attributes == null ? null : attributes.get(qname.substring(1));
		}
		return elements == null ? null : elements.get(qname);
	}

	@Override
	public Map<String, SNode> asMap(String key) {
		return Collections.<String, SNode> singletonMap(opt(key).asString(""), this);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<" + getNodeName());
		if (attributes != null) {
			for (Map.Entry<String, SNode> entry : attributes.entrySet()) {
				sb.append(" " + entry.getKey() + "=\"" + entry.getValue().toString() + "\"");
			}
		}
		sb.append(">");
		if (elements != null) {
			for (Map.Entry<String, SNode> entry : elements.entrySet()) {
				sb.append(entry.getValue().toString());
			}
		} else {
			sb.append(nodeValue);
		}
		sb.append("</" + getNodeName() + ">");
		return sb.toString();
	}

	@Override
	public Iterator<SNode> iterator() {
		return Collections.<SNode> singletonList(this).iterator();
	}

	@Override
	public List<SNode> attritubes() {
		if (attributes == null || attributes.isEmpty()) return super.attritubes();
		List<SNode> list = new ArrayList<SNode>();
		for (SNode v : attributes.values()) {
			list.add(v);
		}
		return list;
	}

	@Override
	public List<SNode> children() {
		if (elements == null || elements.isEmpty()) return super.children();
		List<SNode> list = new ArrayList<SNode>(elementsOrder);
//		for (SNode v : elementsOrder) {
//			list.add(v);
//			switch (v.getNodeType()) {
//				case ELEMENT:
//					list.add(v);
//					break;
//				case ELEMENT_LIST:
//					for (SNode x : v) {
//						list.add(x);
//					}
//					break;
//				default:
//					break;
//			}
//		}
		return list;
	}

	@Override
	protected String getValue() {
		return nodeValue;
	}

	// internal use

	void appendChild(SNode xml) {
		if (xml.isAttribute()) {
			if (attributes == null) attributes = new TreeMap<String, SNode>();
			attributes.put(xml.getNodeName(), xml);
		} else {
			if (nodeValue != null) throw new IllegalStateException("only leaf node can contain text");
			if (elements == null) {
				elements = new TreeMap<String, SNode>();
				elementsOrder = new ArrayList<SNode>();
			}
			elementsOrder.add(xml);
			String qname = xml.getNodeName();
			SNode child = elements.get(qname);
			if (child == null) {
				elements.put(qname, xml);
			} else if (child.getNodeType() == NodeType.ELEMENT_LIST) {
				((SNodeList) child).appendToList(xml);
			} else {
				SNodeList xmllist = new SNodeList();
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
