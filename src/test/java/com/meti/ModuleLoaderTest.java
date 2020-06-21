package com.meti;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static com.meti.ModuleCollection.DEPENDENCIES;
import static com.meti.ModuleCollection.INSTALL;
import static com.meti.ModuleProperty.*;
import static org.junit.jupiter.api.Assertions.*;

public class ModuleLoaderTest {
	private final ModuleLoader moduleLoader = new JsonModuleLoader();

	@Test
	void load() throws IOException {
		try (InputStream resource = getClass().getResourceAsStream("/module.json")) {
			assertNotNull(resource);
			Module module = moduleLoader.load(resource);
			assertEquals("com.meti", module.getProperty(GROUP).orElseThrow());
			assertEquals("test", module.getProperty(NAME).orElseThrow());
			assertEquals("0.1", module.getProperty(VERSION).orElseThrow());
			assertEquals("A description", module.getProperty(DESCRIPTION).orElseThrow());
			assertFalse(module.hasCollection(DEPENDENCIES));
			assertFalse(module.hasCollection(INSTALL));
		}
	}
}
