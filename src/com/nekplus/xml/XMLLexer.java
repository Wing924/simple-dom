package com.nekplus.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.nekplus.xml.EventNode.TokenType;

class XMLLexer {

	public EventReader lex(File f) throws IOException {
		try {
			SAXHander hander = new SAXHander();
			getParser().parse(f, hander);
			return new EventReader(hander.getTokens().iterator());
		} catch (SAXException e) {
			throw new XMLException(e);
		}
	}

	public EventReader lex(InputStream is) throws IOException {
		try {
			SAXHander hander = new SAXHander();
			getParser().parse(is, hander);
			return new EventReader(hander.getTokens().iterator());
		} catch (SAXException e) {
			throw new XMLException(e);
		}
	}

	public EventReader lex(String xmlString) {
		try {
			SAXHander hander = new SAXHander();
			try {
				getParser().parse(new ByteArrayInputStream(xmlString.getBytes()), hander);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return new EventReader(hander.getTokens().iterator());
		} catch (SAXException e) {
			throw new XMLException(e);
		}
	}

	private SAXParser getParser() {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			return factory.newSAXParser();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private class SAXHander extends DefaultHandler {

		private List<EventNode> tokens = new ArrayList<EventNode>();
		private String prevText;
		private TokenType prevType;

		public final List<EventNode> getTokens() {
			return tokens;
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			tokens.add(new EventNode(TokenType.START_TAG, qName, attributes));
			prevType = TokenType.START_TAG;
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			if (prevType == TokenType.START_TAG) {
				prevText = length == 0 ? "" : new String(ch, start, length);
				prevType = TokenType.TEXT;
			} else if (prevType == TokenType.TEXT) {
				prevText += length == 0 ? "" : new String(ch, start, length);
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if (prevType == TokenType.TEXT) {
				tokens.add(new EventNode(TokenType.TEXT, prevText));
				prevText = null;
			}
			tokens.add(new EventNode(TokenType.END_TAG, qName));
			prevType = TokenType.END_TAG;
		}

	}
}
