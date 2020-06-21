package com.meti;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

import static com.meti.ModuleCollection.*;
import static com.meti.ModuleProperty.GROUP;
import static com.meti.ModuleProperty.NAME;
import static java.text.MessageFormat.format;

public class PathModuleInstaller implements ModuleInstaller {
	private final Path directory;
	private final SourceFactory sourceFactory;

	public PathModuleInstaller(Path directory, SourceFactory sourceFactory) {
		this.directory = directory;
		this.sourceFactory = sourceFactory;
	}

	private static void createProcess(String command, ByteArrayOutputStream outputStream,
	                                  ByteArrayOutputStream errorStream) throws InstallException {
		try {
			Process process = new ProcessBuilder(command.split(" ")).start();
			process.getInputStream().transferTo(outputStream);
			process.getErrorStream().transferTo(errorStream);
			process.waitFor();
		} catch (IOException | InterruptedException e) {
			throw new InstallException(format("Failed to start command \"{0}\".", command), e);
		}
	}

	private static void ensure(Path path) throws IOException {
		Path parent = path.getParent();
		if (!Files.exists(parent)) Files.createDirectories(parent);
		if (!Files.exists(path)) Files.createFile(path);
	}

	private static ByteArrayOutputStream execute(String command) throws InstallException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
		createProcess(command, outputStream, errorStream);
		String errorString = errorStream.toString();
		if (!errorString.isBlank()) {
			throw new InstallException(format("Failed to execute command \"{0}\".{1}{2}",
					command,
					System.lineSeparator(),
					errorString));
		}
		return outputStream;
	}

	private static String getName(Module module) {
		return module.getProperty(NAME).orElseThrow(() -> new FormattingException("No name found."));
	}

	private static void transferSourceToPath(Source source, Path child) throws IOException, InstallException {
		InputStream stream = source.open();
		String path = source.formatPath();
		Path value = child.resolve(path);
		transferStreamToPath(stream, value);
	}

	private static void transferStreamToPath(InputStream stream, Path path) throws InstallException {
		try {
			transferStreamToPathExceptionally(stream, path);
		} catch (IOException e) {
			throw new InstallException(format("Failed to write to content location {0}.", path.toAbsolutePath()), e);
		}
	}

	private static void transferStreamToPathExceptionally(InputStream stream, Path path) throws IOException {
		ensure(path);
		OutputStream out = Files.newOutputStream(path);
		stream.transferTo(out);
		out.close();
	}

	private Path formatPath(Module module, String name) throws InstallException {
		Path child = directory.resolve(name);
		if (Files.exists(child)) {
			child = resolvePathClash(module, name);
		}
		try {
			Files.createDirectories(child);
		} catch (IOException e) {
			throw new InstallException(format("Failed to ensure module directory of {0}", child), e);
		}
		return child;
	}

	@Override
	public String install(Module module, ModuleLoader source) throws InstallException {
		String name = getName(module);
		return installWithName(module, source, name);
	}

	private void installDependencies(Module module, ModuleLoader source) throws InstallException {
		Collection<String> dependencies = module.getCollection(DEPENDENCIES);
		for (String dependency : dependencies) {
			installDependency(source, dependency);
		}
	}

	private void installDependency(ModuleLoader loader, String dependency) throws InstallException {
		try (Source source = sourceFactory.from(dependency)) {
			Module dependencyModule = loader.load(source.open());
			install(dependencyModule, loader);
		} catch (MalformedURLException e) {
			throw new InstallException(format("Invalid dependency URL of {0}.", dependency), e);
		} catch (IOException e) {
			throw new InstallException(format("Failed to install {0}.", dependency), e);
		}
	}

	private String installWithName(Module module, ModuleLoader source, String name) throws InstallException {
		Path child = formatPath(module, name);
		transferContents(module, child);
		installDependencies(module, source);
		Collection<String> commands = module.getCollection(INSTALL);
		Collection<String> items = new ArrayList<>();
		for (String command : commands) {
			ByteArrayOutputStream outputStream = execute(command);
			String outputString = outputStream.toString();
			if (!outputString.isBlank()) items.add(outputString);
		}
		return String.join(System.lineSeparator(), items);
	}

	private Path resolvePathClash(Module module, String name) {
		String group = module.getProperty(GROUP).orElseThrow(
				() -> new FormattingException(format("No group found in module {0}.", name)));
		Path other = directory.resolve(group).resolve(name);
		if (!Files.exists(other)) return other;
		throw new FormattingException(format("A module at {0} already exists.", other.toAbsolutePath()));
	}

	private void transferContentToPath(Path child, String content) throws InstallException {
		try (Source source = sourceFactory.from(content)) {
			transferSourceToPath(source, child);
		} catch (IOException e) {
			throw new InstallException(format("Failed to open {0}.", content), e);
		}
	}

	private void transferContents(Module module, Path child) throws InstallException {
		Collection<String> contents = module.getCollection(CONTENT);
		for (String content : contents) {
			transferContentToPath(child, content);
		}
	}
}
