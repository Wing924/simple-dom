package com.nekplus.xml;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.nekplus.xml.EventNode.TokenType;

class XMLParser {

	private EventReader eventReader;

	public XML parse(EventReader eventReader) {
		this.eventReader = eventReader;
		if (!eventReader.hasNext()) return XML.NULL;
		XML xml = buildElement();
		eventReader = null;
		return xml;
	}

	private XML buildElement() {
		EventNode token = eventReader.next();
		if (token.getType() != TokenType.START_TAG) throw new XMLException("Broken XML: first token must be START_TAG");
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
					eventReader.next();
					return element;
				case TEXT:
					String text = nextToken.getValue();
					element.setNodeValue(text);
					eventReader.next();
					nextToken = eventReader.next();
					if (nextToken.getType() != TokenType.END_TAG) throw new XMLException(
							"Broken XML: text node must be in the leaf node");
					return element;
				case START_TAG:
					XML child = buildElement();
					element.appendChild(child);
			}
		}
	}
}
