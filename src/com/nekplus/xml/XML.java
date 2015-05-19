package com.nekplus.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.nekplus.xml.filters.XMLFilter;
import com.nekplus.xml.query.QueryParser;

/**
 * Simple DOM-like XML parser.
 * <table border='1'>
 * <tr>
 * <td>Method \ Node type</td>
 * <td>ELEMENT</td>
 * <td>ELEMENT_LIST</td>
 * <td>ATTRITUBE</td>
 * <td>NULL</td>
 * <td>extra</td>
 * </tr>
 * <tr>
 * <td>get</td>
 * <td>its attritube or child</td>
 * <td>first element's attritube or child</td>
 * <td>NULL</td>
 * <td>NULL</td>
 * <td>to get attribute, use "@" + (name) ex:@id</td>
 * </tr>
 * <td>asTYPE()</td>
 * <td>葉ノードのみ値、それ以外はエラー</td>
 * <td>最初のノードが葉ノードのみ値、それ以外はエラー</td>
 * <td>値</td>
 * <td>エラー</td>
 * <td>変換出来ない場合はエラー</td> </tr>
 * <tr>
 * <td>asTYPE(T nullHack)</td>
 * <td>葉ノードのみ値、それ以外はnullHack</td>
 * <td>最初のノードが葉ノードのみ値、それ以外はnullHack</td>
 * <td>値</td>
 * <td>nullHack</td>
 * <td>変換出来ない場合はnullHack</td>
 * </tr>
 * <tr>
 * <td>iterator</td>
 * <td>自分自身のみ含む反復子</td>
 * <td>リストの反復子</td>
 * <td>空反復子</td>
 * <td>空反復子</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>attritubes</td>
 * <td>属性のリスト</td>
 * <td>最初のノードの属性のリスト</td>
 * <td>空リスト</td>
 * <td>空リスト</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>children</td>
 * <td>子ノードのリスト</td>
 * <td>最初の子ノードのリスト</td>
 * <td>空リスト</td>
 * <td>空リスト</td>
 * <td></td>
 * </tr>
 * </table>
 * 
 * @author weihe
 */
public abstract class XML implements Iterable<XML> {
	/**
	 * node type
	 */
	public enum NodeType {
		/**
		 * element
		 */
		ELEMENT,
		/**
		 * list of element
		 */
		ELEMENT_LIST,
		/**
		 * attribute
		 */
		ATTRITUBE,
		/**
		 * special value for null
		 */
		NULL
	}

	/**
	 * special value for NULL node.
	 */
	public static final XML NULL = new XMLNull();

	private NodeType nodeType;

	private static final String NULL_HACK_STRING = "(null-hack)$";

	/**
	 * Parse XML Document from InputStream
	 * @param is input stream to parse
	 * @return the root node of given XML
	 * @throws IOException when I/O Exception occurred
	 */
	public static XML parse(InputStream is) throws IOException {
		return new XMLParser().parse(new XMLLexer().lex(is));
	}

	/**
	 * Parse XML Document from file
	 * @param file xml file to parse
	 * @return the root node of given XML
	 * @throws IOException when I/O Exception occurred
	 */
	public static XML parse(File file) throws IOException {
		return new XMLParser().parse(new XMLLexer().lex(file));
	}

	/**
	 * Parse XML Document from String
	 * @param xmlString xml String
	 * @return the root node of given XML
	 */
	public static XML parse(String xmlString) {
		return new XMLParser().parse(new XMLLexer().lex(xmlString));
	}

	/**
	 * [internal use]
	 * 
	 * @param nodeType
	 */
	XML(NodeType nodeType) {
		this.nodeType = nodeType;
	}

	/**
	 * Get the node's type
	 * @return the node's type
	 */
	public NodeType getNodeType() {
		return nodeType;
	}

	/**
	 * Get the node's name
	 * @return the node's name
	 */
	public abstract String getNodeName();

	/**
	 * check if is null
	 * @return true: if it is null Node
	 */
	public boolean isNull() {
		return nodeType == NodeType.NULL;
	}

	public boolean isAttribute() {
		return nodeType == NodeType.ATTRITUBE;
	}

	public boolean isElement() {
		return nodeType == NodeType.ELEMENT;
	}

	public boolean isElementList() {
		return nodeType == NodeType.ELEMENT_LIST;
	}

	public boolean isLeafNode() {
		return false;
	}

	public boolean has(String qname) {
		return false;
	}

	public int length() {
		return 1;
	}

	public XML get(String qname) {
		if (qname.charAt(0) == '@') return getAttribute(qname.substring(1));
		if (isLeafNode()) return NULL;
		return getElement(qname);
	}

	public XML get(int index) {
		if (index != 0) return NULL;
		return this;
	}

	public XML filter(XMLFilter filter) {
		if (filter.test(this)) return this;
		return NULL;
	}

	public XML query(String expression) {
		return new QueryParser().eval(this, expression);
	}

	public XML getAttribute(String qname) {
		return NULL;
	}

	public XML getElement(String qname) {
		return NULL;
	}

	@Override
	public Iterator<XML> iterator() {
		return Collections.<XML> emptyList().iterator();
	}

	public List<XML> attritubes() {
		return Collections.emptyList();
	}

	public List<XML> children() {
		return Collections.emptyList();
	}

	public String asString() {
		String value = getValue();
		if (value == null) throw new NullPointerException();
		return value;
	}

	public byte asByte() {
		return Byte.parseByte(asString());
	}

	public short asShort() {
		return Short.parseShort(asString());
	}

	public int asInteger() {
		return Integer.parseInt(asString());
	}

	public long asLong() {
		return Long.parseLong(asString());
	}

	public float asFloat() {
		return Float.parseFloat(asString());
	}

	public double asDouble() {
		return Double.parseDouble(asString());
	}

	public boolean asBoolean() {
		return Boolean.parseBoolean(asString());
	}

	public Date asDate() throws ParseException {
		return DateFormat.getDateInstance().parse(asString());
	}

	public Date asDate(DateFormat dateFormat) throws ParseException {
		return dateFormat.parse(asString());
	}

	public Date asDate(String dateFormat) throws ParseException {
		return new SimpleDateFormat(dateFormat, Locale.getDefault()).parse(asString());
	}

	public <T extends Enum<T>> T asEnum(Class<T> clazz) {
		return Enum.valueOf(clazz, asString());
	}

	public String asString(String nullHack) {
		String s = getValue();
		if (s == null) return nullHack;
		return s;
	}

	public byte asByte(byte nullHack) {
		String s = asString(NULL_HACK_STRING);
		if (s == NULL_HACK_STRING) return nullHack;
		try {
			return Byte.parseByte(s);
		} catch (Exception e) {
			return nullHack;
		}
	}

	public short asShort(short nullHack) {
		String s = asString(NULL_HACK_STRING);
		if (s == NULL_HACK_STRING) return nullHack;
		try {
			return Short.parseShort(s);
		} catch (Exception e) {
			return nullHack;
		}
	}

	public int asInteger(int nullHack) {
		String s = asString(NULL_HACK_STRING);
		if (s == NULL_HACK_STRING) return nullHack;
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			return nullHack;
		}
	}

	public long asLong(long nullHack) {
		String s = asString(NULL_HACK_STRING);
		if (s == NULL_HACK_STRING) return nullHack;
		try {
			return Long.parseLong(s);
		} catch (Exception e) {
			return nullHack;
		}
	}

	public float asFloat(float nullHack) {
		String s = asString(NULL_HACK_STRING);
		if (s == NULL_HACK_STRING) return nullHack;
		try {
			return Float.parseFloat(s);
		} catch (Exception e) {
			return nullHack;
		}
	}

	public double asDouble(double nullHack) {
		String s = asString(NULL_HACK_STRING);
		if (s == NULL_HACK_STRING) return nullHack;
		try {
			return Double.parseDouble(s);
		} catch (Exception e) {
			return nullHack;
		}
	}

	public boolean asBoolean(boolean nullHack) {
		String s = asString(NULL_HACK_STRING);
		if (s == NULL_HACK_STRING) return nullHack;
		try {
			return Boolean.parseBoolean(s);
		} catch (Exception e) {
			return nullHack;
		}
	}

	public Date asDate(Date nullHack) {
		return asDate(DateFormat.getDateInstance(), nullHack);
	}

	public Date asDate(DateFormat dateFormat, Date nullHack) {
		String s = asString(NULL_HACK_STRING);
		if (s == NULL_HACK_STRING) return nullHack;
		try {
			return dateFormat.parse(s);
		} catch (Exception e) {
			return nullHack;
		}
	}

	public Date asDate(String dateFormat, Date nullHack) {
		String s = asString(NULL_HACK_STRING);
		if (s == NULL_HACK_STRING) return nullHack;
		try {
			return new SimpleDateFormat(dateFormat, Locale.US).parse(asString());
		} catch (Exception e) {
			return nullHack;
		}
	}

	public <T extends Enum<T>> T asEnum(Class<T> clazz, T nullHack) {
		String s = asString(NULL_HACK_STRING);
		if (s == NULL_HACK_STRING) return nullHack;
		try {
			T o = Enum.valueOf(clazz, s);
			return o == null ? nullHack : o;
		} catch (Exception e) {
			return nullHack;
		}
	}

	protected abstract String getValue();
}
