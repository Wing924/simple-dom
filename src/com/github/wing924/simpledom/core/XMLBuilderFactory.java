package com.github.wing924.simpledom.core;

import com.github.wing924.simpledom.stream.XMLLexer;

public class XMLBuilderFactory {

	private XMLLexer	lexer;

	public void setXMLLexer(XMLLexer lexer) {
		this.lexer = lexer;
	}

	public XMLBuilder createXmlBuilder() {
		XMLBuilder builder = new XMLBuilder();
		builder.setLexer(lexer);
		return builder;
	}
}
