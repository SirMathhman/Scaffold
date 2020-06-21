package com.meti;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static com.meti.ModuleCollection.*;
import static com.meti.ModuleProperty.NAME;
import static java.util.Collections.singletonMap;
import static org.junit.jupiter.api.Assertions.*;

class PathModuleInstallerTest {
	public static final Path DIRECTORY = Paths.get(".", "test");
	private final ModuleInstaller installer = new PathModuleInstaller(DIRECTORY, singletonMap("url",
			new URLSourceFactory()));

	@AfterAll
	static void cleanup() throws IOException {
		cleanupDirectory(DIRECTORY);
	}

	private static void cleanupDirectory(Path path) throws IOException {
		if (Files.exists(path)) {
			try (Stream<Path> stream = Files.list(path)) {
				stream.forEach(PathModuleInstallerTest::cleanupPath);
			}
			Files.delete(path);
		}
	}

	private static void cleanupPath(Path path) {
		try {
			if (Files.isDirectory(path)) {
				cleanupDirectory(path);
			} else {
				Files.deleteIfExists(path);
			}
		} catch (IOException e) {
			fail("Failed to cleanup " + path.toAbsolutePath(), e);
		}
	}

	@Test
	void install() throws InstallException {
		ObjectMapper mapper = new ObjectMapper();
		Module module = MapModuleBuilder.create()
				.append(NAME, "moduleName")
				.append(CONTENT, "https://pastebin.com/raw/BWSEMbQz")
				.append(DEPENDENCIES, mapper.createObjectNode()
						.put("type", "url")
						.put("value", "https://pastebin.com/raw/M87c5DNr"))
				.append(INSTALL, "cmd /c echo Hello World!")
				.build();
		String output = installer.install(module, new JsonModuleLoader(mapper));
		assertTrue(Files.exists(Paths.get(".", "test", "moduleName", "raw", "BWSEMbQz")));
		assertTrue(Files.exists(Paths.get(".", "test", "test", "raw", "cXA7eYpG")));
		assertEquals(String.format("Hello World!%s", System.lineSeparator()), output);
	}
}