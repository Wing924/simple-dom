package com.github.wing924.simpledom.stream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface XMLLexer {
	
	public EventReader parse(File f) throws IOException;

	public EventReader parse(InputStream is) throws IOException;

	public EventReader parse(String uri) throws IOException;
}
