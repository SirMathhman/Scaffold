package com.meti.source;

import java.nio.file.Paths;

public class PathSourceFactory implements SourceFactory {
	@Override
	public Source from(String value) {
		return new PathSource(Paths.get(value));
	}
}
