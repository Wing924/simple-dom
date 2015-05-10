simple-DOM
==========

The simple and lightweight DOM parser for XML.

Overview
==========

The XML is powerful, but is too complex.
In general, we only use a few features of XML in our development.
Most time, we only want to find nodes and get their values.
The node's order, relation etc. usually needless.

* DOM APIs (org.w3c.dom) have full supporting of XML features, but it is slow and difficult to use.
* SAX APIs (org.xml.sax) are really fast, but it is difficult to use when you handle large complex structure XML.

Features
=========

* simple, simple is the best
* fast to parse XML.
* easy to get nodes' values

Simple XML format
==========

* Only leaf nodes, which have no child element, contain text.
* Only contain start-tag, text, and end-tag.

Example:

```xml
<items>
    <item id="apple">
        <name lang="en">Apple</name>
        <name lang="ja">りんご</name>
        <price>120</price>
        <count>20</count>
    </item>
    <item id="banana">
        <name lang="en">Banana</name>
        <price>150</price>
        <count>10</count>
    </item>
    <item id="pine">
        <name lang="en">Pine</name>
        <price>160</price>
        <count>5</count>
    </item>
</items>
```

Usage
=========

```java
// Parse XML
XML root = XML.parse(/* XML written above */);

// get Apple in Japanese
root.query("item/name(@lang=='ja')").asString(); // りんご

// get banana's price
root.query("item(@id='pine')/price").asInteger(); // 150

// get 3rd item's count
root.query("item[2]/count").asInteger(); // 5

// get number of item
root.get("item").length() // 3
```



