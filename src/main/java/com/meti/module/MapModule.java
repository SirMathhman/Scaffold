package com.meti.module;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.*;

public class MapModule implements Module {
	private final Map<ModuleEntry, Collection<Map<String, String>>> collections;
	private final Map<ModuleList, List<String>> lists;
	private final Map<ModuleProperty, String> properties;

	public MapModule(Map<ModuleProperty, String> properties, Map<ModuleList, List<String>> lists, Map<ModuleEntry,
			Collection<Map<String, String>>> collections) {
		this.properties = unmodifiableMap(properties);
		this.lists = unmodifiableMap(lists);
		this.collections = unmodifiableMap(collections);
	}

	@Override
	public Collection<Map<String, String>> getCollection(ModuleEntry content) {
		return collections.containsKey(content) ?
				unmodifiableCollection(collections.get(content)) :
				emptySet();
	}

	@Override
	public Collection<String> getList(ModuleList list) {
		return lists.getOrDefault(list, emptyList());
	}

	@Override
	public Optional<String> getProperty(ModuleProperty propertyName) {
		return Optional.ofNullable(properties.get(propertyName));
	}

	@Override
	public boolean hasCollection(ModuleEntry collection) {
		return collections.containsKey(collection);
	}

	@Override
	public boolean hasList(ModuleList list) {
		return lists.containsKey(list);
	}
}
