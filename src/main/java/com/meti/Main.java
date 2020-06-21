package com.meti;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
	public static final Path modules = Paths.get(".", "modules");
	private static final Logger logger = Logger.getLogger("Scaffold");
	private final ObjectMapper mapper = new ObjectMapper();
	private final ModuleLoader loader = new JsonModuleLoader(mapper);
	private final Map<String, SourceFactory> sourceFactory = Map.of(
			"path", new PathSourceFactory(),
			"url", new URLSourceFactory()
	);
	private final ModuleInstaller installer = new PathModuleInstaller(modules, sourceFactory);

	public static void main(String[] args) {
		new Main().run();
	}

	private void run() {
		Path path = Paths.get(".", "module.json");
		if (hasBeenCreated(path)) logger.log(Level.INFO, "The template for module.json has been created " +
		                                                 "and as a result will not be installed.");
		else install(path);
	}

	private static boolean hasBeenCreated(Path path) {
		if (Files.exists(path)) return false;
		try {
			createTemplate(path);
			return true;
		} catch (IOException e) {
			logger.log(Level.WARNING, "Failed to create module.json.", e);
		}
		return false;
	}

	private void install(Path path) {
		try (InputStream input = Files.newInputStream(path)) {
			Module module = loader.load(input);
			installer.install(module, loader);
		} catch (IOException | InstallException | FormattingException e) {
			logger.log(Level.SEVERE, "Failed to read module.json", e);
		}
	}

	private static void createTemplate(Path path) throws IOException {
		Files.createFile(path);
		Files.writeString(path, MessageFormat.format("'{'" +
		                                             "{0}  \"group\": ," +
		                                             "{0}  \"name\": ," +
		                                             "{0}  \"version\": \"0.1\"," +
		                                             "{0}  \"description\": ," +
		                                             "{0}  \"content\": []," +
		                                             "{0}  \"dependencies\": []," +
		                                             "{0}  \"install\": []" +
		                                             "'}'",
				System.lineSeparator()));
	}
}
