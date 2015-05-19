package com.nekplus.xml;


class XMLAttribute extends XML {

	private String	qname;
	private String	value;

	public XMLAttribute(String qname, String value) {
		super(NodeType.ATTRITUBE);
		this.value = value;
		this.qname = qname;
	}


	@Override
	public String toString() {
		return value.toString();
	}

	@Override
	protected String getValue() {
		return value;
	}

	@Override
	public boolean isLeafNode() {
		return true;
	}

	@Override
	public String getNodeName() {
		return qname;
	}
}
