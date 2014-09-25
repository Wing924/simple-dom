package com.github.wing924.simpledom.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.wing924.simpledom.core.XML;
import com.github.wing924.simpledom.core.XMLBuilder;
import com.github.wing924.simpledom.core.XMLBuilderFactory;
import com.github.wing924.simpledom.stream.SaxXMLLexer;

public class SimpleTest {

	private static final String	XML_STR	= "<?xml version='1.0' encoding='UTF-8'?>"
												+ "<items xmlns:fx='http://www.example.com'>"
												+ "  <item id='1'>"
												+ "    <cost>123</cost>"
												+ "  </item>"
												+ "  <item id='2'>"
												+ "    <cost fx:name='haha'>234</cost>"
												+ "  </item>"
												+ "</items>";

	@Test
	public void test() throws IOException {
		XMLBuilderFactory factory = new XMLBuilderFactory();
		factory.setXMLLexer(new SaxXMLLexer());
		XMLBuilder builder = factory.createXmlBuilder();
		builder.setInput(new ByteArrayInputStream(XML_STR.getBytes()));
		XML xml = builder.parse();
		assertNotNull(xml);
		System.out.println(xml);
		assertEquals(1, xml.get("item").get("@id").asInteger());
		assertEquals(2, xml.get("item").get(1).get("@id").asInteger());
		assertEquals(5, xml.get("item").get(1).opt("@name").asInteger(5));
		assertEquals(123, xml.get("item").get("cost").asInteger());
		assertEquals("234", xml.get("item").get(1).get("cost").asString());
		assertEquals("haha", xml.get("item").get(1).get("cost").get("@fx:name").asString());
		assertEquals(2, xml.get("item").length());
		assertEquals("items", xml.getNodeName());
	}
}
