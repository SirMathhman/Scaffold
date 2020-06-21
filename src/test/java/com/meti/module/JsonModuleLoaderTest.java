package com.meti.module;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonModuleLoaderTest {

	@Test
	void load() throws IOException {
		ModuleLoader loader = new JsonModuleLoader();
		String content = "{\"name\":\"test\"}";
		byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
		ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
		Module module = loader.load(stream);
		Optional<String> property = module.getProperty(ModuleProperty.NAME);
		assertTrue(property.isPresent());
		assertEquals("test", property.get());
	}
}