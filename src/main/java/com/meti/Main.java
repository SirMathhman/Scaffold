package com.meti;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meti.module.Module;
import com.meti.module.*;
import com.meti.source.PathSourceFactory;
import com.meti.source.SourceFactory;
import com.meti.source.URLSourceFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
	public static final Path MODULES = Paths.get(".", "modules");
	public static final Path PROPERTIES = Paths.get(".", "config.properties");
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
	private Properties properties = new Properties();

	private static void deleteLogged(Path path) {
		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			logger.log(Level.WARNING, "Failed to delete " + path, e);
		}
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

	private void run() {
		setupLogger();
		loadSettings();
		deletePrevious();
		install();
	}

	private static void setupLogger() {
		logger.setUseParentHandlers(false);
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		logger.addHandler(handler);
	}

	private void loadSettings() {
		if (!Files.exists(PROPERTIES)) {
			logger.log(Level.WARNING, "Properties file does not exist. It will be created.");
			try {
				Files.createFile(PROPERTIES);
			} catch (IOException e) {
				logger.log(Level.WARNING, "Failed to create properties file.", e);
			}
		}
		try (InputStream stream = Files.newInputStream(PROPERTIES)) {
			logger.log(Level.FINE, "Loading in properties");
			properties.load(stream);
		} catch (IOException e) {
			logger.log(Level.WARNING, "Failed to load properties from config file.", e);
		}

		Level level = getProperty(properties, "Log Level")
				.map(s -> s.toUpperCase(Locale.ENGLISH))
				.map(Level::parse)
				.orElse(Level.ALL);
		logger.setLevel(level);
	}

	private static void deletePrevious() {
		logger.log(Level.INFO, "Deleting previous installed content.");
		try {
			Files.walkFileTree(MODULES, new DeleteFileVisitor());
		} catch (IOException e) {
			logger.log(Level.WARNING, String.format("Failed to walk file tree of %s", MODULES), e);
		}
		logger.log(Level.FINE, "Finished deleting previous content.");
	}

	private void install() {
		Path path = Paths.get(".", "module.json");
		if (hasBeenCreated(path)) {
			logger.log(Level.WARNING, "The template for module.json has been created " +
			                          "and as a result will not be installed.");
		} else {
			logger.log(Level.INFO, "Attempting to install module.json.");
			install(path);
		}
	}

	private Optional<String> getProperty(Properties properties, String key) {
		return Optional.ofNullable(properties.getProperty(key));
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

	private static void createTemplate(Path path) throws IOException {
		String separator = System.lineSeparator();
		String formattedTemplate = MessageFormat.format(TEMPLATE, separator);
		Files.createFile(path);
		Files.writeString(path, formattedTemplate);
	}

	private static class DeleteFileVisitor extends SimpleFileVisitor<Path> {
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			Files.delete(file);
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			Files.delete(dir);
			return FileVisitResult.CONTINUE;
		}
	}
}
