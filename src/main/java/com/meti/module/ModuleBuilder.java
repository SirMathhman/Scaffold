package com.meti.module;

import java.util.Map;

public interface ModuleBuilder {
	ModuleBuilder append(ModuleList list, String value);

	ModuleBuilder append(ModuleProperty property, String value);

	ModuleBuilder append(ModuleEntry entry, Map<String, String> value);

	Module build();
}
