package com.github.wing924.simpledom.stream;

import java.util.Iterator;

public class IteratorEventReader implements EventReader {

	private Iterator<EventNode>	iter;

	private EventNode			peekNode;
	
	

	public IteratorEventReader(Iterator<EventNode> iter) {
		this.iter = iter;
	}

	@Override
	public boolean hasNext() {
		return peekNode != null || iter.hasNext();
	}

	@Override
	public EventNode next() {
		if (peekNode != null) {
			EventNode tmp = peekNode;
			peekNode = null;
			return tmp;
		}
		return iter.next();
	}

	@Override
	public EventNode peek() {
		if (peekNode != null) throw new IllegalStateException();
		return peekNode = iter.next();
	}
}
