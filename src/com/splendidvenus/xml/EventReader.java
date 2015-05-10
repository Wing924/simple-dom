package com.splendidvenus.xml;

import java.util.Iterator;

class EventReader implements Iterator<EventNode> {

	private Iterator<EventNode> iter;

	private EventNode peekNode;

	public EventReader(Iterator<EventNode> iter) {
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

	public EventNode peek() {
		if (peekNode != null) return peekNode;
		return peekNode = iter.next();
	}
}
