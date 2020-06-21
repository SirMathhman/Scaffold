package com.meti.module;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.*;

public class JsonModule implements Module {
	private final JsonNode value;

	public JsonModule(JsonNode value) {
		this.value = value;
	}

	@Override
	public Collection<?> getCollection(ModuleCollection content) {
		String name = content.name();
		String formattedName = name.toLowerCase(Locale.ENGLISH);
		JsonNode node = value.get(formattedName);
		if (null == node) return Collections.emptySet();
		Collection<Object> toReturn = new ArrayList<>();
		for (int i = 0; i < node.size(); i++) {
			JsonNode child = node.get(i);
			if (child.isTextual()) {
				toReturn.add(child.asText());
			} else {
				toReturn.add(child);
			}
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
