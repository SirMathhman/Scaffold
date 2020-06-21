package com.meti.module;

public interface ModuleBuilder {
	ModuleBuilder append(ModuleProperty property, String value);

	ModuleBuilder append(ModuleCollection collection, Object value);

	Module build();
}
