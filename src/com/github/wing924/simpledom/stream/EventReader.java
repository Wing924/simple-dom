package com.github.wing924.simpledom.stream;

public interface EventReader {
	
	public boolean hasNext();

	public EventNode next();

	public EventNode peek();
}
