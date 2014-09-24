package com.github.wing924.simpledom.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.github.wing924.simpledom.core.XML.XMLNodeType;
import com.github.wing924.simpledom.frontend.XMLLexer;
import com.github.wing924.simpledom.frontend.XMLToken;
import com.github.wing924.simpledom.frontend.XMLToken.TokenType;
import com.github.wing924.simpledom.utils.Assert;

public class XMLBuilder {

	private XMLLexer				lexer;
	private LinkedList<XMLToken>	tokens;
	private ListIterator<XMLToken>	iter;

	public XMLBuilder setInput(File f) throws SAXException, IOException {
		tokens = lexer.parse(f);
		return this;
	}

	public XMLBuilder setInput(InputSource is) throws SAXException, IOException {
		tokens = lexer.parse(is);
		return this;
	}

	public XMLBuilder setInput(InputStream is) throws SAXException, IOException {
		tokens = lexer.parse(is);
		return this;
	}

	public XMLBuilder setInput(String uri) throws SAXException, IOException {
		tokens = lexer.parse(uri);
		return this;
	}

	public XML parse() {
		if (tokens == null) throw new NullPointerException("please set input source");
		if (tokens.isEmpty()) return XML.NULL_NODE;
		iter = tokens.listIterator();
		XML xml = buildElement();
		tokens = null;
		iter = null;
		return xml;
	}

	private XML buildElement() {
		XMLToken token = iter.next();
		Assert.equals(TokenType.START_TAG, token.getType());
		String qname = token.getValue();

		List<Entry<String, String>> attributes = token.getAttributes();
		if (attributes == null) {
			XMLToken nextToken = iter.next();
			switch (nextToken.getType()) {
				case END_TAG:
					return new XMLValue(XMLNodeType.ELEMENT, qname, "");
				case TEXT:
					XMLToken nextToken2 = iter.next();
					Assert.equals(TokenType.END_TAG, nextToken2.getType());
					return new XMLValue(XMLNodeType.ELEMENT, qname, nextToken.getValue());
				case START_TAG:
					iter.previous();
			}
		}

		XMLElement element = new XMLElement(qname);

		if (attributes != null) {
			for (Map.Entry<String, String> entry : attributes) {
				String attrQname = entry.getKey();
				String attrValue = entry.getValue();
				element.appendAttritube(attrQname, attrValue);
			}
		}

		boolean hasChildren = false;

		for (;;) {
			XMLToken nextToken = iter.next();
			switch (nextToken.getType()) {
				case END_TAG:
					if (hasChildren) return element;
					return new XMLValue(XMLNodeType.ELEMENT, qname, "");
				case TEXT:
					XMLToken nextToken2 = iter.next();
					Assert.equals(TokenType.END_TAG, nextToken2.getType());
					return new XMLValue(XMLNodeType.ELEMENT, qname, nextToken.getValue());
				case START_TAG:
					iter.previous();
					XML child = buildElement();
					element.appendElement(child);
					hasChildren = true;
			}
		}
	}
}
