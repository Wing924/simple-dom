package com.github.wing924.simpledom.core;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
 * <td>getAttritube / getElement</td>
 * <td>ノードの属性/子ノード (ない場合はエラー)</td>
 * <td>最初のノードの属性/子ノード (ない場合はエラー)</td>
 * <td>エラー</td>
 * <td>エラー</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>optAttritube / optElement</td>
 * <td>ノードの属性/子ノード (ない場合はNULL_NODE)</td>
 * <td>最初のノードの属性/子ノード (ない場合はNULL_NODE)</td>
 * <td>NULL_NODE</td>
 * <td>NULL_NODE</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>asMap</td>
 * <td>{key -> 自分自身}</td>
 * <td>{key1 -> node1, ..., keyN -> nodeN}</td>
 * <td>空マップ</td>
 * <td>空マップ</td>
 * <td>keyが"@"開始ならば属性、以外なら子ノード</td>
 * </tr>
 * <td>asXXX (XXXは型)</td>
 * <td>葉ノードのみ値、それ以外はエラー</td>
 * <td>最初のノードが葉ノードのみ値、それ以外はエラー</td>
 * <td>値</td>
 * <td>エラー</td>
 * <td>変換出来ない場合はエラー</td>
 * </tr>
 * <tr>
 * <td>optXXX (XXXは型)</td>
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
	public enum XMLNodeType {
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

	private XMLNodeType			nodeType;
	private String				qname;

	private static final String	NULL_HACK_STRING	= "(null-hack)";

	public XML(XMLNodeType nodeType, String qname) {
		this.nodeType = nodeType;
		this.qname = qname;
	}

	public XMLNodeType getNodeType() {
		return nodeType;
	}

	public String getNodeName() {
		return qname;
	}

	/**
	 * nullかどうか
	 * 
	 * @return
	 */
	public boolean isNull() {
		return nodeType == XMLNodeType.NULL;
	}

	public XML get(String qname) {
		throw new UnsupportedOperationException("className = " + getClass().getSimpleName());
	}

	public XML opt(String qname) {
		return NULL_NODE;
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
	 * <pre>
	 * &lt;names&gt;
	 *   &lt;name lang="ja"&gt;雲&lt;/name&gt;
	 *   &lt;name lang="en"&gt;cloud&lt;/name&gt;
	 * &lt;/names&gt;
	 * </pre>
	 * を変換すると
	 * {"ja" -> "雲", "en" -> "cloud"}
	 * @param key
	 * @return
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
		return new SimpleDateFormat(dateFormat, Locale.US).parse(asString());
	}

	public <T extends Enum<T>> T asEnum(Class<T> clazz) {
		return Enum.valueOf(clazz, asString());
	}

	public String optString() {
		return optString("");
	}

	public String optString(String nullHack) {
		String s = getValue();
		if (s == null) return nullHack;
		return s;
	}

	public byte optByte() {
		return optByte((byte) 0);
	}

	public byte optByte(byte nullHack) {
		String s = optString(NULL_HACK_STRING);
		if (s == NULL_HACK_STRING) return nullHack;
		try {
			return Byte.parseByte(s);
		} catch (Exception e) {
			return nullHack;
		}
	}

	public short optShort() {
		return optShort((short) 0);
	}

	public short optShort(short nullHack) {
		String s = optString(NULL_HACK_STRING);
		if (s == NULL_HACK_STRING) return nullHack;
		try {
			return Short.parseShort(s);
		} catch (Exception e) {
			return nullHack;
		}
	}

	public int optInteger() {
		return optInteger(0);
	}

	public int optInteger(int nullHack) {
		String s = optString(NULL_HACK_STRING);
		if (s == NULL_HACK_STRING) return nullHack;
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			return nullHack;
		}
	}

	public long optLong() {
		return optLong(0L);
	}

	public long optLong(long nullHack) {
		String s = optString(NULL_HACK_STRING);
		if (s == NULL_HACK_STRING) return nullHack;
		try {
			return Long.parseLong(s);
		} catch (Exception e) {
			return nullHack;
		}
	}

	public float optFloat() {
		return optFloat(0.0f);
	}

	public float optFloat(float nullHack) {
		String s = optString(NULL_HACK_STRING);
		if (s == NULL_HACK_STRING) return nullHack;
		try {
			return Float.parseFloat(s);
		} catch (Exception e) {
			return nullHack;
		}
	}

	public double optDouble() {
		return optDouble(0.0);
	}

	public double optDouble(double nullHack) {
		String s = optString(NULL_HACK_STRING);
		if (s == NULL_HACK_STRING) return nullHack;
		try {
			return Double.parseDouble(s);
		} catch (Exception e) {
			return nullHack;
		}
	}

	public boolean optBoolean() {
		return optBoolean(false);
	}

	public boolean optBoolean(boolean nullHack) {
		String s = optString(NULL_HACK_STRING);
		if (s == NULL_HACK_STRING) return nullHack;
		try {
			return Boolean.parseBoolean(s);
		} catch (Exception e) {
			return nullHack;
		}
	}

	public Date optDate() {
		return optDate((Date) null);
	}

	public Date optDate(Date nullHack) {
		return optDate(DateFormat.getDateInstance());
	}

	public Date optDate(DateFormat dateFormat) {
		return optDate(dateFormat, null);
	}

	public Date optDate(DateFormat dateFormat, Date nullHack) {
		String s = optString(NULL_HACK_STRING);
		if (s == NULL_HACK_STRING) return nullHack;
		try {
			return dateFormat.parse(s);
		} catch (Exception e) {
			return nullHack;
		}
	}

	public Date optDate(String dateFormat) {
		return optDate(dateFormat, null);
	}

	public Date optDate(String dateFormat, Date nullHack) {
		String s = optString(NULL_HACK_STRING);
		if (s == NULL_HACK_STRING) return nullHack;
		try {
			return new SimpleDateFormat(dateFormat, Locale.US).parse(asString());
		} catch (Exception e) {
			return nullHack;
		}
	}

	public <T extends Enum<T>> T optEnum(Class<T> clazz, T nullHack) {
		String s = optString(NULL_HACK_STRING);
		if (s == NULL_HACK_STRING) return nullHack;
		try {
			T o = Enum.valueOf(clazz, s);
			return o == null ? nullHack : o;
		} catch (Exception e) {
			return nullHack;
		}

	}

	protected abstract String getValue();

	public static final XML	NULL_NODE	= new NullValue();

	private static class NullValue extends XML {

		public NullValue() {
			super(XMLNodeType.NULL, "");
		}

		@Override
		public String toString() {
			return "(null)";
		}

		@Override
		protected String getValue() {
			return null;
		}
	}
}
