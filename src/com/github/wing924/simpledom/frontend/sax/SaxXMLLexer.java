package com.github.wing924.simpledom.frontend.sax;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.github.wing924.simpledom.core.XMLException;
import com.github.wing924.simpledom.frontend.XMLLexer;
import com.github.wing924.simpledom.frontend.XMLToken;
import com.github.wing924.simpledom.frontend.XMLToken.TokenType;

public class SaxXMLLexer implements XMLLexer {

	@Override
	public LinkedList<XMLToken> parse(File f) throws IOException {
		try {
			SAXHander hander = new SAXHander();
			getParser().parse(f, hander);
			return hander.getTokens();
		} catch (SAXException e) {
			throw new XMLException(e);
		}
	}

	@Override
	public LinkedList<XMLToken> parse(InputSource is) throws IOException {
		try {
			SAXHander hander = new SAXHander();
			getParser().parse(is, hander);
			return hander.getTokens();
		} catch (SAXException e) {
			throw new XMLException(e);
		}
	}

	@Override
	public LinkedList<XMLToken> parse(InputStream is) throws IOException {
		try {
			SAXHander hander = new SAXHander();
			getParser().parse(is, hander);
			return hander.getTokens();
		} catch (SAXException e) {
			throw new XMLException(e);
		}
	}

	@Override
	public LinkedList<XMLToken> parse(String uri) throws IOException {
		try {
			SAXHander hander = new SAXHander();
			getParser().parse(uri, hander);
			return hander.getTokens();
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

		private LinkedList<XMLToken>	tokens	= new LinkedList<XMLToken>();

		public final LinkedList<XMLToken> getTokens() {
			return tokens;
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			boolean emptyStr = true;
			for (int i = 0; i < length; i++) {
				if (ch[start + i] > ' ') {
					emptyStr = false;
					break;
				}
			}
			if (!emptyStr) {
				tokens.add(new XMLToken(TokenType.TEXT, new String(ch, start, length)));
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			tokens.add(new XMLToken(TokenType.END_TAG, qName));
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			tokens.add(new XMLToken(TokenType.START_TAG, qName, attributes));
		}
	}
}