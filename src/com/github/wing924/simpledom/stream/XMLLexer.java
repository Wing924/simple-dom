package com.github.wing924.simpledom.stream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface XMLLexer {
	
	public EventReader lex(File f) throws IOException;

	public EventReader lex(InputStream is) throws IOException;

	public EventReader lex(String uri) throws IOException;
}
