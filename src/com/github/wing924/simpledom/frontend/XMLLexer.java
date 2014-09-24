package com.github.wing924.simpledom.frontend;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import org.xml.sax.InputSource;

public interface XMLLexer {
	public LinkedList<XMLToken> parse(File f) throws IOException;

	public LinkedList<XMLToken> parse(InputSource is) throws IOException;

	public LinkedList<XMLToken> parse(InputStream is) throws IOException;

	public LinkedList<XMLToken> parse(String uri) throws IOException;
}
