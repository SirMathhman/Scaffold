package com.meti.source;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class URLSource implements Source {
	private final URL url;
	private InputStream stream = null;

	public URLSource(URL url) {
		this.url = url;
	}

	@Override
	public void close() throws IOException {
		if (stream == null) {
			throw new IllegalStateException("URL has not been opened yet.");
		}
		stream.close();
	}

	@Override
	public InputStream open() throws IOException {
		return stream = url.openStream();
	}

	@Override
	public String formatPath() {
		String path = url.getPath();
		String formatSlashes = path.replace('/', '\\');
		return formatSlashes.substring(1);
	}
}
