package com.github.wing924.simpledom.core;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class XMLElementList extends XML {

	private List<XML>	list	= new LinkedList<XML>();

	public XMLElementList(String qname) {
		super(XMLNodeType.ELEMENT_LIST, qname);
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
	public Map<String, XML> asMap(String key) {
		if (key == null || key.length() == 0 || key.equals("@")) throw new NullPointerException("key is null or empty");
		boolean isAttr = key.charAt(0) == '@';
		if (isAttr) key = key.substring(1);

		Map<String, XML> map = new LinkedHashMap<String, XML>();
		for (XML xml : list) {
			XML value = xml.opt(key);
			map.put(value.optString(""), xml);
		}
		return map;
	}

	@Override
	public Iterator<XML> iterator() {
		return list.iterator();
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
