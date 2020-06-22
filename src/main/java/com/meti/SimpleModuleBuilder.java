package com.meti;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class SimpleModuleBuilder implements ModuleBuilder {
	private final String artifact;
	private final Collection<Module> children;
	private final Collection<Source> content;
	private final Collection<Source> dependencies;
	private final Collection<String> enter;
	private final Collection<String> exit;
	private final String group;
	private final Collection<Source> tasks;
	private final String version;

	public SimpleModuleBuilder() {
		this(null, null, null,
				Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
				Collections.emptyList(), Collections.emptyList(), Collections.emptyList()
		);
	}

	public SimpleModuleBuilder(String group, String artifact, String version,
	                           Collection<Source> content, Collection<Source> dependencies, Collection<Source> tasks,
	                           Collection<String> enter, Collection<String> exit, Collection<Module> children) {
		this.artifact = artifact;
		this.children = new ArrayList<>(children);
		this.content = new ArrayList<>(content);
		this.dependencies = new ArrayList<>(dependencies);
		this.enter = new ArrayList<>(enter);
		this.exit = new ArrayList<>(exit);
		this.group = group;
		this.tasks = new ArrayList<>(tasks);
		this.version = version;
	}

	@Override
	public Module build() {
		return new SimpleModule(group, artifact, version, content, dependencies, tasks, enter, exit, children);
	}

	@Override
	public ModuleBuilder withArtifact(String artifact) {
		return new SimpleModuleBuilder(group, artifact, version, content, dependencies, tasks, enter, exit, children);
	}

	@Override
	public ModuleBuilder withChild(Module child) {
		this.children.add(child);
		return this;
	}

	@Override
	public ModuleBuilder withContent(Source content) {
		this.content.add(content);
		return this;
	}

	@Override
	public ModuleBuilder withDependency(Source dependency) {
		this.dependencies.add(dependency);
		return this;
	}

	@Override
	public ModuleBuilder withEnter(String enter) {
		this.enter.add(enter);
		return this;
	}

	@Override
	public ModuleBuilder withExit(String exit) {
		this.exit.add(exit);
		return this;
	}

	@Override
	public ModuleBuilder withGroup(String group) {
		return new SimpleModuleBuilder(group, artifact, version, content, dependencies, tasks, enter, exit, children);
	}

	@Override
	public ModuleBuilder withTask(Source task) {
		this.tasks.add(task);
		return this;
	}

	@Override
	public ModuleBuilder withVersion(String version) {
		return new SimpleModuleBuilder(group, artifact, version, content, dependencies, tasks, enter, exit, children);
	}
}
