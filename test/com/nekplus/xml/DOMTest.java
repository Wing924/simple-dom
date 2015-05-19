package com.nekplus.xml;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nekplus.xml.XML;

public class DOMTest {

	private static final int N = 10000;

	private static String XML1;
	private static String XML2;

	private Document dom1, dom2;
	private XML xml1, xml2;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		XML1 = "<list>\n";
		for (int i = 0; i < N; i++) {
			XML1 += "<item name='#" + i + "'>item" + i + "</item>\n";
		}
		XML1 += "</list>";

		XML2 = "<list>\n";
		for (int i = 0; i < N; i++) {
			XML2 += String.format("<item%d>value%d</item%d>", i, i, i);
		}
		XML2 += "</list>";
	}

	@Before
	public void setUp() throws Exception {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		dom1 = builder.parse(new ByteArrayInputStream(XML1.getBytes()));
		dom2 = builder.parse(new ByteArrayInputStream(XML2.getBytes()));
		xml1 = XML.parse(XML1);
		xml2 = XML.parse(XML2);
	}

	@Test
	public void buildDOM() throws Exception {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		dom1 = builder.parse(new ByteArrayInputStream(XML1.getBytes()));
	}

	@Test
	public void buildXML() throws Exception {
		xml1 = XML.parse(XML1);
	}

	@Test
	public void DOM1first() {
		Element e = dom1.getDocumentElement();
		for (int i = 0; i < N; i++) {
			e.getElementsByTagName("item").item(0).getFirstChild().getTextContent();
		}
	}

	@Test
	public void DOM1last() {
		Element e = dom1.getDocumentElement();
		for (int i = 0; i < 10000; i++) {
			e.getElementsByTagName("item").item(N - 1).getFirstChild().getTextContent();
		}
	}

	@Test
	public void DOM2first() {
		Element e = dom2.getDocumentElement();
		for (int i = 0; i < N; i++) {
			e.getElementsByTagName("item0").item(0).getFirstChild().getTextContent();
		}
	}

	@Test
	public void DOM2last() {
		Element e = dom2.getDocumentElement();
		for (int i = 0; i < 10000; i++) {
			e.getElementsByTagName("item" + (N - 1)).item(0).getFirstChild().getTextContent();
		}
	}

	@Test
	public void XML1first() {
		for (int i = 0; i < N; i++) {
			xml1.get("item").get(0).asString();
		}
	}

	@Test
	public void XML1last() {
		for (int i = 0; i < N; i++) {
			xml1.get("item").get(N - 1).asString();
		}
	}

	@Test
	public void XML2first() {
		for (int i = 0; i < N; i++) {
			xml2.get("item0").asString();
		}
	}

	@Test
	public void XML2last() {
		for (int i = 0; i < N; i++) {
			xml2.get("item" + (N - 1)).asString();
		}
	}

}
