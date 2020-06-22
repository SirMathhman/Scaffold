package com.meti;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meti.module.Module;
import com.meti.module.*;
import com.meti.source.PathSourceFactory;
import com.meti.source.SourceFactory;
import com.meti.source.URLSourceFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
	public static final Path MODULES = Paths.get(".", "modules");
	public static final String TEMPLATE = "'{'" +
	                                      "{0}  \"group\": ," +
	                                      "{0}  \"name\": ," +
	                                      "{0}  \"version\": \"0.1\"," +
	                                      "{0}  \"description\": ," +
	                                      "{0}  \"content\": []," +
	                                      "{0}  \"dependencies\": []," +
	                                      "{0}  \"install\": []" +
	                                      "'}'";
	public static final Path modules = Paths.get(".", "modules");
	private static final Logger logger = Logger.getLogger("Scaffold");
	private final ObjectMapper mapper = new ObjectMapper();
	private final ModuleLoader loader = new JsonModuleLoader(mapper);
	private final Map<String, SourceFactory> sourceFactory = Map.of(
			"path", new PathSourceFactory(),
			"url", new URLSourceFactory()
	);
	private final ModuleInstaller installer = new PathModuleInstaller(modules, sourceFactory);

	private static void createTemplate(Path path) throws IOException {
		String separator = System.lineSeparator();
		String formattedTemplate = MessageFormat.format(TEMPLATE, separator);
		Files.createFile(path);
		Files.writeString(path, formattedTemplate);
	}

	private static void deleteLogged(Path path) {
		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			logger.log(Level.WARNING, "Failed to delete " + path, e);
		}
	}

	private static void deletePrevious() {
		logger.log(Level.INFO, "Deleting previous installed content.");
		deleteRecursively(MODULES);
		logger.log(Level.FINE, "Finished deleting previous content.");
	}

	private static void deleteRecursively(Path path) {
		if (Files.isDirectory(path)) {
			Stream<Path> stream = list(path);
			List<Path> children = stream.collect(Collectors.toList());
			for (Path child : children) {
				deleteRecursively(child);
			}
		}
		deleteLogged(path);
	}

	private static boolean hasBeenCreated(Path path) {
		if (Files.exists(path)) {
			logger.log(Level.FINE, path + " already exists, don't need to create a new one.");
			return false;
		}
		try {
			logger.log(Level.FINE, String.format("Attempting to create template at %s", path));
			createTemplate(path);
			logger.log(Level.FINE, String.format("Created template at %s successfully.", path));
			return true;
		} catch (IOException e) {
			logger.log(Level.WARNING, "Failed to create module.json.", e);
			return false;
		}
	}

	private static Stream<Path> list(Path path) {
		Stream<Path> stream = null;
		try {
			stream = Files.list(path);
		} catch (IOException e) {
			logger.log(Level.WARNING, "Failed to find children of " + path, e);
		}
		return stream;
	}

	public static void main(String[] args) {
		new Main().run();
	}

	private void install(Path path) {
		logger.log(Level.INFO, "Attempting to read module at " + path);
		try (InputStream input = Files.newInputStream(path)) {
			Module module = loader.load(input);

			Optional<String> nameOptional = module.getProperty(ModuleProperty.NAME);
			if (nameOptional.isPresent()) {
				String message = module.toString();
				logger.log(Level.INFO, message);
			} else {
				logger.log(Level.WARNING, String.format("Module at %s apparently doesn't have a name. " +
				                                        "You should give it one.", path));
			}

			installer.install(module, loader);
		} catch (IOException | InstallException | FormattingException e) {
			logger.log(Level.SEVERE, String.format("Failed to install module at %s", path), e);
		}
	}

	private void run() {
		deletePrevious();

		Path path = Paths.get(".", "module.json");
		if (hasBeenCreated(path)) {
			logger.log(Level.WARNING, "The template for module.json has been created " +
			                          "and as a result will not be installed.");
		} else {
			logger.log(Level.INFO, "Attempting to install module.json.");
			install(path);
		}
	}
}
