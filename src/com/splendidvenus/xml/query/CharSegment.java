package com.splendidvenus.xml.query;

class CharSegment implements CharSequence {

	private char[] array;
	private int offset;
	private int length;

	public CharSegment(char[] array) {
		this(array, 0, array.length);
	}

	public CharSegment(char[] array, int offset) {
		this(array, offset, array.length - offset);
	}

	public CharSegment(char[] array, int offset, int length) {
		this.array = array;
		this.offset = offset;
		this.length = length;
	}

	@Override
	public int length() {
		return length;
	}

	@Override
	public char charAt(int index) {
		return array[offset + index];
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return new CharSegment(array, offset + start, offset + end);
	}

	@Override
	public String toString() {
		return new String(array, offset, length);
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof CharSequence)) return false;
		return toString().equals(obj.toString());
	}

}
