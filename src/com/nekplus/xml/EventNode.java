package com.nekplus.xml;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.xml.sax.Attributes;

class EventNode {

	public enum TokenType {
		START_TAG, END_TAG, TEXT
	}

	private TokenType type;
	private String value = "";
	private List<Map.Entry<String, String>> attributes;

	public EventNode(TokenType type, String value) {
		this.type = type;
		this.value = value;
	}

	public EventNode(TokenType type, String value, Attributes attributes) {
		this.type = type;
		this.value = value;
		if (attributes != null && attributes.getLength() > 0) {
			int count = attributes.getLength();
			this.attributes = new ArrayList<Map.Entry<String, String>>(count);
			for (int i = 0; i < count; i++) {
				this.attributes.add(new AbstractMap.SimpleEntry<String, String>
						(attributes.getQName(i), attributes.getValue(i)));
			}
		}
	}

	public final TokenType getType() {
		return type;
	}

	public final String getValue() {
		return value;
	}

	public final List<Entry<String, String>> getAttributes() {
		return attributes;
	}

	@Override
	public String toString() {
		switch (type) {
			case END_TAG:
				return "[END TAG  ] </" + value + ">";
			case START_TAG:
				if (attributes == null) return "[START TAG] <" + value + ">";
				StringBuilder sb = new StringBuilder();
				sb.append("[START TAG] <" + value);
				for (Map.Entry<String, String> entry : attributes) {
					sb.append(" " + entry.getKey() + "='" + entry.getValue() + "'");
				}
				sb.append(">");
				return sb.toString();
			case TEXT:
				return "[TEXT     ] '" + value + "'";
		}
		return "";
	}
}
