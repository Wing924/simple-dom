package com.github.wing924.simpledom.stream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.github.wing924.simpledom.core.SimpleDomException;
import com.github.wing924.simpledom.stream.EventNode.TokenType;

public class SaxLexer implements XMLLexer {

	@Override
	public EventReader lex(File f) throws IOException {
		try {
			SAXHander hander = new SAXHander();
			getParser().parse(f, hander);
			return new IteratorEventReader(hander.getTokens().iterator());
		} catch (SAXException e) {
			throw new SimpleDomException(e);
		}
	}

	@Override
	public EventReader lex(InputStream is) throws IOException {
		try {
			SAXHander hander = new SAXHander();
			getParser().parse(is, hander);
			return new IteratorEventReader(hander.getTokens().iterator());
		} catch (SAXException e) {
			throw new SimpleDomException(e);
		}
	}

	@Override
	public EventReader lex(String uri) throws IOException {
		try {
			SAXHander hander = new SAXHander();
			getParser().parse(uri, hander);
			return new IteratorEventReader(hander.getTokens().iterator());
		} catch (SAXException e) {
			throw new SimpleDomException(e);
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

		private LinkedList<EventNode>	tokens	= new LinkedList<EventNode>();
		private String					prevText;
		private TokenType				prevType;

		public final LinkedList<EventNode> getTokens() {
			return tokens;
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			if (prevType == TokenType.START_TAG) {
				prevText = length == 0 ? "" : new String(ch, start, length);
				prevType = TokenType.TEXT;
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

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			tokens.add(new EventNode(TokenType.START_TAG, qName, attributes));
			prevType = TokenType.START_TAG;
		}
	}
}
