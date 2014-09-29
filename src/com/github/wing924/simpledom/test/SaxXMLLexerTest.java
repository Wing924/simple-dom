package com.github.wing924.simpledom.test;

import java.io.ByteArrayInputStream;

import org.junit.Test;

import com.github.wing924.simpledom.stream.EventReader;
import com.github.wing924.simpledom.stream.SaxLexer;

public class SaxXMLLexerTest {

	@SuppressWarnings("unused")
	private static final String	XML_STR		= "<?xml version='1.0' encoding='UTF-8'?>"
													+ "<items xmlns:fx='http://www.example.com'>"
													+ "  <item id='1'>"
													+ "    <cost>123</cost>"
													+ "  </item>"
													+ "  <item id='2'>"
													+ "    <cost fx:name='haha'>234</cost>"
													+ "  </item>"
													+ "</items>";

	private static final String	XML_STR2	= "<?xml version='1.0' encoding='UTF-8'?>"
													+ "<items xmlns:fx='http://www.example.com'>"
													+ "  <item id='1'>"
													+ "    nonono"
													+ "    <cost>123</cost>"
													+ "    ahhaha"
													+ "  </item>"
													+ "   hh"
													+ "  <item id='2'>"
													+ "    <cost fx:name='haha'>234</cost>"
													+ "  </item>"
													+ "</items>";

	@Test
	public void test() throws Exception {
		EventReader reader = new SaxLexer().lex(new ByteArrayInputStream(XML_STR2.getBytes()));
		while (reader.hasNext()) {
			System.out.println(reader.next());
		}
	}

}
