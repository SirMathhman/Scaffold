package com.meti;

import java.util.Collection;
import java.util.Optional;

public interface Module {
	Collection<?> getCollection(ModuleCollection content);

	Optional<String> getProperty(ModuleProperty propertyName);

	boolean hasCollection(ModuleCollection collection);
}
