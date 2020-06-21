package com.meti;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

public class PathSource implements Source {
	private final Path child;
	private final Path root = Paths.get(".");
	private InputStream stream = null;

	public PathSource(Path child) {
		this.child = child;
	}

	@Override
	public void close() throws IOException {
		if (null != stream) stream.close();
	}

	@Override
	public InputStream open() throws IOException {
		return stream = Files.newInputStream(child);
	}

	@Override
	public String formatPath() {
		Path relative = root.relativize(child);
		Collection<String> names = new ArrayList<>();
		for (int i = 0; i < relative.getNameCount(); i++) {
			names.add(relative.getName(i).toString());
		}
		return String.join("\\", names);
	}
}
