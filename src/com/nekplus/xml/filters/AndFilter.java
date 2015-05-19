package com.nekplus.xml.filters;

import com.nekplus.xml.XML;

public class AndFilter implements XMLFilter {
	private XMLFilter lhs;
	private XMLFilter rhs;

	public AndFilter(XMLFilter lhs, XMLFilter rhs) {
		super();
		this.lhs = lhs;
		this.rhs = rhs;
	}

	@Override
	public boolean test(XML node) {
		return lhs.test(node) && rhs.test(node);
	}

}
