package com.meti;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

public class MapModuleBuilder implements ModuleBuilder {
	private final Map<ModuleCollection, Collection<Object>> collections = new EnumMap<>(ModuleCollection.class);
	private final Map<ModuleProperty, String> properties = new EnumMap<>(ModuleProperty.class);

	public static ModuleBuilder create() {
		return new MapModuleBuilder();
	}

	@Override
	public ModuleBuilder append(ModuleProperty property, String value) {
		if (properties.containsKey(property))
			throw new IllegalArgumentException(property + " has already been assigned.");
		properties.put(property, value);
		return this;
	}

	@Override
	public ModuleBuilder append(ModuleCollection collection, Object value) {
		if (!collections.containsKey(collection)) collections.put(collection, new ArrayList<>());
		collections.get(collection).add(value);
		return this;
	}

	@Override
	public Module build() {
		return new MapModule(properties, collections);
	}
}
