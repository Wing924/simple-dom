package com.nekplus.xml;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.nekplus.xml.XML;
import com.nekplus.xml.XML.NodeType;
import com.nekplus.xml.filters.CompareFilter;

public class XMLTest {

	private static final int N = 10000;

	private static final String XML_STR = "<?xml version='1.0' encoding='UTF-8'?>"
			+ "<items xmlns:fx='http://www.example.com'>"
			+ "  <item id='1'>"
			+ "    <cost>123</cost>"
			+ "  </item>"
			+ "  <item id='2'>"
			+ "    <cost fx:name='haha'>234</cost>"
			+ "  </item>"
			+ "</items>";
	private static XML xml1, xml2;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		xml1 = XML.parse(XML_STR);

		String XML2 = "<list>\n";
		for (int i = 0; i < N; i++) {
			XML2 += String.format("<item id='%d'>value%d</item>", i, i);
		}
		XML2 += "</list>";

		xml2 = XML.parse(XML2);
	}

	@Test
	public void testGetNodeType() {
		assertEquals(NodeType.ELEMENT, xml1.getNodeType());
		assertEquals(NodeType.ELEMENT_LIST, xml1.get("item").getNodeType());
		assertEquals(NodeType.ELEMENT, xml1.get("item").get(0).getNodeType());
		assertEquals(NodeType.ATTRITUBE, xml1.get("item").get(1).get("@id").getNodeType());
		assertEquals(NodeType.NULL, xml1.get("noexist").getNodeType());
		assertEquals(NodeType.NULL, xml1.get("item").get(100).getNodeType());
	}

	@Test
	public void testGetNodeName() {
		assertEquals("items", xml1.getNodeName());
		assertEquals("item", xml1.get("item").getNodeName());
		assertEquals("item", xml1.get("item").get(0).getNodeName());
		assertEquals("id", xml1.get("item").get(1).get("@id").getNodeName());
		assertNull(xml1.get("noexist").getNodeName());
		assertNull(xml1.get("item").get(100).getNodeName());
	}

	@Test
	public void testIsNull() {
		assertFalse(xml1.isNull());
		assertFalse(xml1.get("item").isNull());
		assertFalse(xml1.get("item").get(0).isNull());
		assertFalse(xml1.get("item").get(1).get("@id").isNull());
		assertTrue(xml1.get("noexist").isNull());
		assertTrue(xml1.get("item").get(100).isNull());
	}

	@Test
	public void testIsLeafNode() {
		assertFalse(xml1.isLeafNode());
		assertFalse(xml1.get("item").isLeafNode());
		assertFalse(xml1.get("item").get(1).isLeafNode());
		assertTrue(xml1.get("item").get("@id").isLeafNode());
		assertTrue(xml1.get("item").get(1).get("cost").isLeafNode());
	}

	@Test
	public void testHas() {
		assertTrue(xml1.has("item"));
		assertTrue(xml1.get("item").get(1).has("@id"));
		assertFalse(xml1.has("noexist"));
	}

	@Test
	public void testLength() {
		assertEquals(1, xml1.length());
		assertEquals(2, xml1.get("item").length());
		assertEquals(0, xml1.get("noexist").length());
	}

	// @Test
	// public void testGetString() {
	// fail("Not yet implemented");
	// }

	// @Test
	// public void testGetInt() {
	// fail("Not yet implemented");
	// }

	@Test
	public void testFilter() {
		assertEquals("value10", xml2.get("item").filter(new CompareFilter("@id", "==", 10)).asString());
		assertEquals(10, xml2.get("item").filter(new CompareFilter("@id", "<", 10)).length());
		assertEquals(11, xml2.get("item").filter(new CompareFilter("@id", "<=", 10)).length());
		assertTrue(xml2.get("item").filter(new CompareFilter("@id", "==", "a")).isNull());
	}

	@Test
	public void testQuery() {
		assertEquals("value3", xml2.query("item(@id==3)").asString());
		assertEquals("value6", xml2.query("item(@id > 3)[2]").asString());
		assertEquals(3, xml2.query("item(@id > 3 && @id < 7)").length());
	}

	// @Test
	// public void testIterator() {
	// fail("Not yet implemented");
	// }
	//
	//
	// @Test
	// public void testAsString() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testAsByte() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testAsShort() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testAsInteger() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testAsLong() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testAsFloat() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testAsDouble() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testAsBoolean() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testAsDate() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testAsDateDateFormat() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testAsDateString() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testAsEnumClassOfT() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testAsStringString() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testAsByteByte() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testAsShortShort() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testAsIntegerInt() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testAsLongLong() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testAsFloatFloat() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testAsDoubleDouble() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testAsBooleanBoolean() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testAsDateDate() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testAsDateDateFormatDate() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testAsDateStringDate() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testAsEnumClassOfTT() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testGetValue() {
	// fail("Not yet implemented");
	// }

}
