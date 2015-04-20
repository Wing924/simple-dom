package com.github.wing924.simpledom.stream;

import java.util.Iterator;

public interface EventReader extends Iterator<EventNode> {
	
	public EventNode peek();
}
