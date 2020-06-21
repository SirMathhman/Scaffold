package com.meti.module;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface Module {
	Collection<Map<String, String>> getCollection(ModuleEntry content);

	Collection<String> getList(ModuleList list);

	Optional<String> getProperty(ModuleProperty propertyName);

	boolean hasCollection(ModuleEntry collection);

	boolean hasList(ModuleList list);
}
