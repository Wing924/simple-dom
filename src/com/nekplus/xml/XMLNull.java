package com.nekplus.xml;

class XMLNull extends XML {

	public XMLNull() {
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
