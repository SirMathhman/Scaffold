package com.meti.module;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface Module {
	Collection<Map<String, String>> getCollection(ModuleCollection content);

	Optional<String> getProperty(ModuleProperty propertyName);

	boolean hasCollection(ModuleCollection collection);
}
