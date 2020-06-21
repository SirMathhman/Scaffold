package com.meti.source;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public interface Source extends Closeable {
	InputStream open() throws IOException;

	String formatPath();
}
