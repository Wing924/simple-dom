package com.github.wing924.simpledom.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class SNodeList extends SNode {

	private List<SNode>	list	= new ArrayList<SNode>();

	public SNodeList() {
		super(NodeType.ELEMENT_LIST);
	}

	@Override
	public String getNodeName() {
		return getFirst().getNodeName();
	}

	@Override
	public boolean isLeafNode() {
		return getFirst().isLeafNode();
	}

	@Override
	public SNode get(String qname) {
		return getFirst().get(qname);
	}

	@Override
	public SNode opt(String qname) {
		return optFirst().opt(qname);
	}

	@Override
	public int length() {
		return list.size();
	}

	@Override
	public SNode get(int index) {
		return list.get(index);
	}

	@Override
	public SNode opt(int index) {
		if (index < 0 || index >= length()) return NULL_NODE;
		return list.get(index);
	}

	@Override
	public Map<String, SNode> asMap(String key) {
		Map<String, SNode> map = new LinkedHashMap<String, SNode>();
		for (SNode xml : list) {
			SNode value = xml.opt(key);
			map.put(value.asString(""), xml);
		}
		return map;
	}

	@Override
	public Iterator<SNode> iterator() {
		return list.iterator();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (SNode xml : list) {
			sb.append(xml.toString());
		}
		return sb.toString();
	}

	void appendToList(SNode xml) {
		list.add(xml);
	}

	private SNode getFirst() {
		if (list.isEmpty()) throw new NullPointerException("empty node list");
		return list.get(0);
	}
	
	private SNode optFirst() {
		if(list.isEmpty()) return NULL_NODE;
		return list.get(0);
	}

	@Override
	public List<SNode> children() {
		return getFirst().children();
	}

	@Override
	public List<SNode> attritubes() {
		return getFirst().attritubes();
	}

	@Override
	protected String getValue() {
		return getFirst().getValue();
	}

}
