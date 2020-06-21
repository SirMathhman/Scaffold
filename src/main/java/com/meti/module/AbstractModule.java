package com.meti.module;

import java.text.MessageFormat;

public abstract class AbstractModule implements Module {
	@Override
	public String toString() {
		String name = getProperty(ModuleProperty.NAME).orElse("none");
		String group = getProperty(ModuleProperty.GROUP).orElse("unspecified");
		String version = getProperty(ModuleProperty.VERSION).orElse("unspecified");
		String description = getProperty(ModuleProperty.DESCRIPTION).orElse("No description given.");
		int content = getCollection(ModuleEntry.CONTENT).size();
		int dependencies = getCollection(ModuleEntry.DEPENDENCIES).size();
		int size = getList(ModuleList.INSTALL).size();
		return MessageFormat.format("Installing module {0} with group \"{1}\" and version \"{2}\"" +
		                            ".{7}" +
		                            "\tDescription: {3}{7}" +
		                            "\tContent Count: {4}{7}" +
		                            "\tDependency Count: {5}{7}" +
		                            "\tInstallation Command Count: {6}{7}",
				name, group, version, description, content, dependencies, size, System.lineSeparator());
	}
}
