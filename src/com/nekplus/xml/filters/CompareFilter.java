package com.nekplus.xml.filters;

import com.nekplus.xml.XML;

public class CompareFilter implements XMLFilter {

	private enum Operator {
		EQ, NE, GT, GTE, LT, LTE
	}

	private String name;
	private Operator op;
	private String strValue = null;
	private Long intValue = null;
	private Double doubleValue = null;

	public CompareFilter(String name, String operator, String value) {
		this.name = name;
		this.strValue = value;
		op = parseOperator(operator);
	}

	public CompareFilter(String name, String operator, long value) {
		this.name = name;
		this.intValue = value;
		op = parseOperator(operator);
	}

	public CompareFilter(String name, String operator, double value) {
		this.name = name;
		this.doubleValue = value;
		op = parseOperator(operator);
	}

	private Operator parseOperator(String operator) {
		if (operator.equals("==")) return Operator.EQ;
		if (operator.equals(">")) return Operator.GT;
		if (operator.equals(">=")) return Operator.GTE;
		if (operator.equals("<")) return Operator.LT;
		if (operator.equals("<=")) return Operator.LTE;
		if (operator.equals("!=")) return Operator.NE;
		throw new IllegalArgumentException("unknown operator: " + operator);
	}

	@Override
	public boolean test(XML node) {
		XML o = node.get(name);
		if (!o.isLeafNode()) return false;
		int cmp = compare(o);
		switch (op) {
			case EQ:
				return cmp == 0;
			case GT:
				return cmp > 0;
			case GTE:
				return cmp >= 0;
			case LT:
				return cmp < 0;
			case LTE:
				return cmp <= 0;
			case NE:
				return cmp != 0;
			default:
				return false;
		}
	}

	private int compare(XML node) {
		if (strValue != null) return node.asString().compareTo(strValue);
		if (intValue != null) return Long.compare(node.asLong(), intValue);
		return Double.compare(node.asDouble(), doubleValue);
	}
}
