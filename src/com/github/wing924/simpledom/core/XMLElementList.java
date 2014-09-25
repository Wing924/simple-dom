package com.github.wing924.simpledom.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class XMLElementList extends XML {

	private List<XML>	list	= new ArrayList<XML>();

	public XMLElementList(String qname) {
		super(XMLNodeType.ELEMENT_LIST, qname);
	}

	@Override
	public boolean isLeafNode() {
		return list.get(0).isLeafNode();
	}

	@Override
	public XML get(String qname) {
		return getFirst().get(qname);
	}

	@Override
	public XML opt(String qname) {
		return getFirst().opt(qname);
	}

	@Override
	public int length() {
		return list.size();
	}

	@Override
	public XML get(int index) {
		return list.get(index);
	}

	@Override
	public XML opt(int index) {
		if (index < 0 || index >= length()) return NULL_NODE;
		return list.get(index);
	}

	@Override
	public Map<String, XML> asMap(String key) {
		Map<String, XML> map = new LinkedHashMap<String, XML>();
		for (XML xml : list) {
			XML value = xml.opt(key);
			map.put(value.asString(""), xml);
		}
		return map;
	}

	@Override
	public Iterator<XML> iterator() {
		return list.iterator();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (XML xml : list) {
			sb.append(xml.toString());
		}
		return sb.toString();
	}

	void appendToList(XML xml) {
		list.add(xml);
	}

	private XML getFirst() {
		return list.get(0);
	}

	@Override
	public List<XML> children() {
		return getFirst().children();
	}

	@Override
	public List<XML> attritubes() {
		return getFirst().attritubes();
	}

	@Override
	protected String getValue() {
		return getFirst().getValue();
	}

}
