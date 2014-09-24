package com.github.wing924.simpledom.core;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.wing924.simpledom.stream.EventNode;
import com.github.wing924.simpledom.stream.EventNode.TokenType;
import com.github.wing924.simpledom.stream.EventReader;
import com.github.wing924.simpledom.utils.Assert;

public class XMLBuilder {

	private EventReader	eventReader;

	public XML parse() {
		if (eventReader == null) throw new NullPointerException("please set input source");
		if (!eventReader.hasNext()) return XML.NULL_NODE;
		XML xml = buildElement();
		eventReader = null;
		return xml;
	}

	private XML buildElement() {
		EventNode token = eventReader.next();
		Assert.equals(TokenType.START_TAG, token.getType());
		String qname = token.getValue();

		XMLElement element = new XMLElement(qname);

		List<Entry<String, String>> attributes = token.getAttributes();

		if (attributes != null) {
			for (Map.Entry<String, String> entry : attributes) {
				String attrQname = entry.getKey();
				String attrValue = entry.getValue();
				element.appendChild(new XMLAttribute(attrQname, attrValue));
			}
		}

		for (;;) {
			EventNode nextToken = eventReader.peek();
			switch (nextToken.getType()) {
				case END_TAG:
					return element;
				case TEXT:
					String text = nextToken.getValue();
					eventReader.next();
					nextToken = eventReader.next();
					Assert.equals(TokenType.END_TAG, nextToken.getType());
					return new XMLElement(qname, text);
				case START_TAG:
					XML child = buildElement();
					element.appendChild(child);
			}
		}
	}
}
