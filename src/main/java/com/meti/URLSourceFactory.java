package com.meti;

import java.net.MalformedURLException;
import java.net.URL;

public class URLSourceFactory implements SourceFactory {
	@Override
	public Source from(String value) throws InstallException {
		try {
			return new URLSource(new URL(value));
		} catch (MalformedURLException e) {
			throw new InstallException("Failed to construct source.", e);
		}
	}
}
