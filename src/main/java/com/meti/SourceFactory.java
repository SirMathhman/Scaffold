package com.meti;

public interface SourceFactory {
	Source from(String value) throws InstallException;
}
