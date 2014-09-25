package com.github.wing924.simpledom.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.wing924.simpledom.stream.EventNode;
import com.github.wing924.simpledom.stream.EventNode.TokenType;
import com.github.wing924.simpledom.stream.EventReader;
import com.github.wing924.simpledom.stream.XMLLexer;

public class XMLBuilder {

	private XMLLexer	lexer;
	private EventReader	eventReader;

	XMLBuilder() {
	}

	final void setLexer(XMLLexer lexer) {
		this.lexer = lexer;
	}

	public void setInput(File f) throws IOException {
		eventReader = lexer.parse(f);
	}

	public void setInput(InputStream is) throws IOException {
		eventReader = lexer.parse(is);
	}

	public void setInput(String uri) throws IOException {
		eventReader = lexer.parse(uri);
	}

	public XML parse() {
		if (eventReader == null) throw new NullPointerException("please set input source");
		if (!eventReader.hasNext()) return XML.NULL_NODE;
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
