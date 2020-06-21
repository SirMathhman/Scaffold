package com.meti;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.*;

public class MapModule implements Module {
	private final Map<ModuleCollection, Collection<Object>> collections;
	private final Map<ModuleProperty, String> properties;

	public MapModule(Map<ModuleProperty, String> properties, Map<ModuleCollection, Collection<Object>> collections) {
		this.properties = unmodifiableMap(properties);
		this.collections = unmodifiableMap(collections);
	}

	@Override
	public Collection<?> getCollection(ModuleCollection content) {
		return collections.containsKey(content) ?
				unmodifiableCollection(collections.get(content)) :
				emptySet();
	}

	@Override
	public Optional<String> getProperty(ModuleProperty propertyName) {
		return Optional.ofNullable(properties.get(propertyName));
	}

	@Override
	public boolean hasCollection(ModuleCollection collection) {
		return collections.containsKey(collection);
	}
}
