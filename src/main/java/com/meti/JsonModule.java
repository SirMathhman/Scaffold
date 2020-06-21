package com.meti;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.*;

public class JsonModule implements Module {
	private final JsonNode value;

	public JsonModule(JsonNode value) {
		this.value = value;
	}

	@Override
	public Collection<String> getCollection(ModuleCollection content) {
		String name = content.name();
		String formattedName = name.toLowerCase(Locale.ENGLISH);
		JsonNode node = value.get(formattedName);
		if (node == null) return Collections.emptySet();
		Collection<String> toReturn = new ArrayList<>();
		for (int i = 0; i < node.size(); i++) {
			toReturn.add(node.get(i).asText());
		}
		return toReturn;
	}

	@Override
	public Optional<String> getProperty(ModuleProperty propertyName) {
		String name = propertyName.name();
		String formattedName = name.toLowerCase(Locale.ENGLISH);
		return Optional.ofNullable(value.get(formattedName)).map(JsonNode::asText);
	}

	@Override
	public boolean hasCollection(ModuleCollection collection) {
		String name = collection.name();
		String formattedName = name.toLowerCase(Locale.ENGLISH);
		JsonNode node = value.get(formattedName);
		return null != node && !node.isEmpty();
	}
}
