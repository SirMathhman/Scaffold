package com.meti;

import java.util.Collection;

import static java.util.Collections.unmodifiableCollection;

public class SimpleModule implements Module {
	private final String artifact;
	private final Collection<Module> children;
	private final Collection<Source> content;
	private final Collection<Source> dependencies;
	private final Collection<String> enter;
	private final Collection<String> exit;
	private final String group;
	private final Collection<Source> tasks;
	private final String version;

	public SimpleModule(String group, String artifact, String version, Collection<Source> content,
	                    Collection<Source> dependencies, Collection<Source> tasks, Collection<String> enter,
	                    Collection<String> exit, Collection<Module> children) {
		this.group = group;
		this.artifact = artifact;
		this.version = version;
		this.content = unmodifiableCollection(content);
		this.dependencies = unmodifiableCollection(dependencies);
		this.tasks = unmodifiableCollection(tasks);
		this.enter = unmodifiableCollection(enter);
		this.exit = unmodifiableCollection(exit);
		this.children = unmodifiableCollection(children);
	}

	@Override
	public String artifact() {
		return artifact;
	}

	@Override
	public Collection<Module> children() {
		return children;
	}

	@Override
	public Collection<Source> content() {
		return content;
	}

	@Override
	public Collection<Source> dependencies() {
		return dependencies;
	}

	@Override
	public Collection<String> enter() {
		return enter;
	}

	@Override
	public Collection<String> exit() {
		return exit;
	}

	@Override
	public String group() {
		return group;
	}

	@Override
	public Collection<Source> tasks() {
		return tasks;
	}

	@Override
	public String version() {
		return version;
	}
}
