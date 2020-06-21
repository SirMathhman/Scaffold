package com.meti.module;

import java.io.IOException;
import java.io.InputStream;

public interface ModuleLoader {
	Module load(InputStream resource) throws IOException;
}
