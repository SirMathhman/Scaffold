package com.meti.module;

import java.util.*;

public class MapModuleBuilder implements ModuleBuilder {
	private final Map<ModuleEntry, Collection<Map<String, String>>> collections = new EnumMap<>(ModuleEntry.class);
	private final Map<ModuleList, List<String>> lists = new EnumMap<>(ModuleList.class);
	private final Map<ModuleProperty, String> properties = new EnumMap<>(ModuleProperty.class);

	public static ModuleBuilder create() {
		return new MapModuleBuilder();
	}

	@Override
	public ModuleBuilder append(ModuleList list, String value) {
		if (!lists.containsKey(list)) lists.put(list, new ArrayList<>());
		lists.get(list).add(value);
		return this;
	}

	@Override
	public ModuleBuilder append(ModuleProperty property, String value) {
		if (properties.containsKey(property))
			throw new IllegalArgumentException(property + " has already been assigned.");
		properties.put(property, value);
		return this;
	}

	@Override
	public ModuleBuilder append(ModuleEntry entry, Map<String, String> value) {
		if (!collections.containsKey(entry)) collections.put(entry, new ArrayList<>());
		collections.get(entry).add(value);
		return this;
	}

	@Override
	public Module build() {
		return new MapModule(properties, lists, collections);
	}
}
