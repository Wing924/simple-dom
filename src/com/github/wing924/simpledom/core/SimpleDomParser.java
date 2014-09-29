package com.github.wing924.simpledom.core;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.wing924.simpledom.stream.EventNode;
import com.github.wing924.simpledom.stream.EventNode.TokenType;
import com.github.wing924.simpledom.stream.EventReader;

public class SimpleDomParser {

	private EventReader	eventReader;

	public SNode parse(EventReader eventReader) {
		this.eventReader = eventReader;
		if (eventReader == null) throw new NullPointerException("please set input source");
		if (!eventReader.hasNext()) return SNode.NULL_NODE;
		SNode xml = buildElement();
		eventReader = null;
		return xml;
	}

	private SNode buildElement() {
		EventNode token = eventReader.next();
		if (token.getType() != TokenType.START_TAG) throw new SimpleDomException("Broken XML: first token must be START_TAG");
		String qname = token.getValue();

		SElementNode element = new SElementNode(qname);

		List<Entry<String, String>> attributes = token.getAttributes();

		if (attributes != null) {
			for (Map.Entry<String, String> entry : attributes) {
				String attrQname = entry.getKey();
				String attrValue = entry.getValue();
				element.appendChild(new SAttributeNode(attrQname, attrValue));
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
					if (nextToken.getType() != TokenType.END_TAG) throw new SimpleDomException(
							"Broken XML: text node must be in the leaf node");
					return element;
				case START_TAG:
					SNode child = buildElement();
					element.appendChild(child);
			}
		}
	}
}
