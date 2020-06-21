package com.meti;

public interface ModuleBuilder {
	ModuleBuilder append(ModuleProperty property, String value);

	ModuleBuilder append(ModuleCollection collection, String value);

	Module build();
}
