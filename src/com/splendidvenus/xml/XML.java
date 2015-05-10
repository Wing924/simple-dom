package com.splendidvenus.xml;

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
import java.util.Map;

import com.splendidvenus.xml.filters.XMLFilter;
import com.splendidvenus.xml.query.QueryParser;

/**
 * シンプルなDOMライクのXMLパーサー。java DOMより軽量で、SAXやXmlPullParserより可読性良い。
 * <table border='1'>
 * <tr>
 * <td></td>
 * <td>ELEMENT</td>
 * <td>ELEMENT_LIST</td>
 * <td>ATTRITUBE</td>
 * <td>NULL</td>
 * <td>補足</td>
 * </tr>
 * <tr>
 * <td>get</td>
 * <td>ノードの属性/子ノード (ない場合はエラー)</td>
 * <td>最初のノードの属性/子ノード (ない場合はエラー)</td>
 * <td>エラー</td>
 * <td>エラー</td>
 * <td>属性の場合は"@" + (名前)。例：@id</td>
 * </tr>
 * <tr>
 * <td>opt</td>
 * <td>ノードの属性/子ノード (ない場合はNULL_NODE)</td>
 * <td>最初のノードの属性/子ノード (ない場合はNULL_NODE)</td>
 * <td>NULL_NODE</td>
 * <td>NULL_NODE</td>
 * <td>属性の場合は"@" + (名前)。例：@id</td>
 * </tr>
 * <tr>
 * <td>asMap</td>
 * <td>{key -> 自分自身}</td>
 * <td>{key1 -> node1, ..., keyN -> nodeN}</td>
 * <td>空マップ</td>
 * <td>空マップ</td>
 * <td>keyが"@"開始ならば属性、以外なら子ノード</td>
 * </tr>
 * <td>asXXX() (XXXは型)</td>
 * <td>葉ノードのみ値、それ以外はエラー</td>
 * <td>最初のノードが葉ノードのみ値、それ以外はエラー</td>
 * <td>値</td>
 * <td>エラー</td>
 * <td>変換出来ない場合はエラー</td> </tr>
 * <tr>
 * <td>asXXX(T nullHack) (XXXは型)</td>
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
	 * ノードタイプ
	 * 
	 * @author weihe
	 */
	public enum NodeType {
		/**
		 * エレメント
		 */
		ELEMENT,
		/**
		 * エレメントのリスト
		 */
		ELEMENT_LIST,
		/**
		 * 属性
		 */
		ATTRITUBE,
		/**
		 * nullの特別値
		 */
		NULL
	}

	public static final XML NULL = new NullValue();

	private NodeType nodeType;

	private static final String NULL_HACK_STRING = "(null-hack)$";

	public static XML parse(InputStream is) throws IOException {
		return new XMLParser().parse(new XMLLexer().lex(is));
	}

	public static XML parse(File file) throws IOException {
		return new XMLParser().parse(new XMLLexer().lex(file));
	}

	public static XML parse(String xmlString) {
		return new XMLParser().parse(new XMLLexer().lex(xmlString));
	}

	XML(NodeType nodeType) {
		this.nodeType = nodeType;
	}

	public NodeType getNodeType() {
		return nodeType;
	}

	public abstract String getNodeName();

	/**
	 * nullかどうか
	 * 
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

	/**
	 * ELEMENT_LISTのみ有用
	 * 
	 * <pre>
	 * &lt;names&gt;
	 *   &lt;name lang="ja"&gt;雲&lt;/name&gt;
	 *   &lt;name lang="en"&gt;cloud&lt;/name&gt;
	 * &lt;/names&gt;
	 * </pre>
	 * 
	 * を変換すると
	 * {"ja" -> "雲", "en" -> "cloud"}
	 * 
	 * @param key
	 * @return key-to-value Map
	 */
	public Map<String, XML> asMap(String key) {
		throw new UnsupportedOperationException();
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

	private static class NullValue extends XML {

		public NullValue() {
			super(NodeType.NULL);
		}

		@Override
		public int length() {
			return 0;
		}

		@Override
		public String toString() {
			return "(null)";
		}

		@Override
		protected String getValue() {
			return null;
		}

		@Override
		public String getNodeName() {
			return null;
		}
	}
}
