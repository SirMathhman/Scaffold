package com.meti.source;

import com.meti.module.InstallException;

public interface SourceFactory {
	Source from(String value) throws InstallException;
}
