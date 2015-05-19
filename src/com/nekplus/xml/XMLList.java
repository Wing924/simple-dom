package com.nekplus.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.nekplus.xml.filters.XMLFilter;

class XMLList extends XML {

	private List<XML> list;

	public XMLList() {
		super(NodeType.ELEMENT_LIST);
		list = new ArrayList<XML>();
	}

	public XMLList(List<XML> list) {
		super(NodeType.ELEMENT_LIST);
		this.list = list;
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
	public XML getAttribute(String qname) {
		return optFirst().getAttribute(qname);
	}

	@Override
	public XML getElement(String qname) {
		return getFirst().getElement(qname);
	}

	@Override
	public int length() {
		return list.size();
	}

	@Override
	public XML get(int index) {
		if (index < 0 || index >= length()) return NULL;
		return list.get(index);
	}

	@Override
	public XML filter(XMLFilter filter) {
		List<XML> filtered = new ArrayList<XML>();
		for (XML node : list) {
			if (filter.test(node)) filtered.add(node);
		}
		if (filtered.isEmpty()) return NULL;
		return new XMLList(filtered);
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
		if (list.isEmpty()) throw new NullPointerException("empty node list");
		return list.get(0);
	}

	private XML optFirst() {
		if (list.isEmpty()) return NULL;
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
