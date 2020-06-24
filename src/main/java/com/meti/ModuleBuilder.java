package com.meti;

public interface ModuleBuilder {
	Module build();

	ModuleBuilder withArtifact(String artifact);

	ModuleBuilder withChild(Module child);

	ModuleBuilder withContent(Source content);

	ModuleBuilder withDependency(Source dependency);

	ModuleBuilder withEnter(String enter);

	ModuleBuilder withExit(String exit);

	ModuleBuilder withGroup(String group);

	ModuleBuilder withTask(Source task);

	ModuleBuilder withVersion(String version);
}
