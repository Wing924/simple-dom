package com.splendidvenus.xml;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

public class SimpleTest {

	private static final String XML_STR = "<?xml version='1.0' encoding='UTF-8'?>"
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
		XML xml = XML.parse(XML_STR);
		assertEquals(1, xml.get("item").get("@id").asInteger());
		assertEquals(1, xml.get("item").get(0).get("@id").asInteger());
		assertEquals(2, xml.get("item").get(1).get("@id").asInteger());
		assertEquals(5, xml.get("item").get(1).get("@name").asInteger(5));
		assertEquals(123, xml.get("item").get("cost").asInteger());
		assertEquals("234", xml.get("item").get(1).get("cost").asString());
		assertEquals("haha", xml.get("item").get(1).get("cost").get("@fx:name").asString());
		assertEquals(2, xml.get("item").length());
		assertEquals("items", xml.getNodeName());
		
		assertEquals(1, xml.query("item/@id").asInteger());
		assertEquals(1, xml.query("item[0]/@id").asInteger());
		assertEquals(2, xml.query("item[1]/@id").asInteger());
		assertEquals(5, xml.query("item[1]/@name").asInteger(5));
		assertEquals(123, xml.query("item/cost").asInteger());
		assertEquals("234", xml.query("item[1]/cost").asString());
		assertEquals("haha", xml.query("item[1]/cost/@fx:name").asString());
		assertEquals(2, xml.query("item").length());
	}
}
