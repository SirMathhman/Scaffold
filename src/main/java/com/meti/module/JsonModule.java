package com.meti.module;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.text.MessageFormat.format;

public class JsonModule extends AbstractModule {
	private final JsonNode root;

	public JsonModule(JsonNode root) {
		this.root = root;
	}

	@Override
	public Collection<Map<String, String>> getCollection(ModuleEntry content) {
		String collectionName = content.name();
		String formattedName = collectionName.toLowerCase(Locale.ENGLISH);
		JsonNode collectionNode = root.get(formattedName);
		return null == collectionNode ?
				Collections.emptySet() :
				convertChildrenToCheckedMaps(collectionNode);
	}

	@Override
	public Collection<String> getList(ModuleList list) {
		String listName = list.name();
		String formattedName = listName.toLowerCase(Locale.ENGLISH);
		JsonNode collectionNode = root.get(formattedName);
		return null == collectionNode ?
				Collections.emptySet() :
				convertChildrenToList(collectionNode);
	}

	private static List<String> convertChildrenToList(JsonNode parent) {
		return IntStream.range(0, parent.size())
				.mapToObj(parent::get)
				.map(JsonNode::asText)
				.collect(Collectors.toList());
	}

	private static List<Map<String, String>> convertChildrenToCheckedMaps(JsonNode parent) {
		return IntStream.range(0, parent.size())
				.mapToObj(parent::get)
				.map(JsonModule::convertNodeToCheckedMap)
				.collect(Collectors.toList());
	}

	private static Map<String, String> convertNodeToCheckedMap(JsonNode node) {
		if (node.isObject()) {
			return convertNodeToMap(node);
		} else {
			throw new IllegalArgumentException(String.format("%s is not an object.", node));
		}
	}

	private static Map<String, String> convertNodeToMap(JsonNode node) {
		Iterator<String> fieldNames = node.fieldNames();
		Map<String, String> fields = convertNodeToMap(node, fieldNames);
		if (fields.isEmpty()) {
			throw new IllegalArgumentException(String.format("No fields were found in %s", node));
		}
		return fields;
	}

	private static Map<String, String> convertNodeToMap(JsonNode node, Iterator<String> names) {
		Map<String, String> entryMap = new HashMap<>();
		while (names.hasNext()) {
			String name = names.next();
			String text = extractTextNullable(node, name);
			entryMap.put(name, text);
		}
		return entryMap;
	}

	private static String extractTextNullable(JsonNode node, String fieldName) {
		JsonNode value = node.get(fieldName);
		if (null == value) {
			throw new IllegalStateException(format("{0} was listed as a field for {1}, but was" +
			                                       " not found.", fieldName, node));
		}
		return extractText(value);
	}

	private static String extractText(JsonNode value) {
		String text;
		if (value.isTextual()) {
			text = value.asText();
		} else {
			throw new IllegalArgumentException(value + " is not text.");
		}
		return text;
	}

	@Override
	public Optional<String> getProperty(ModuleProperty propertyName) {
		String name = propertyName.name();
		String formattedName = name.toLowerCase(Locale.ENGLISH);
		return Optional.ofNullable(root.get(formattedName)).map(JsonNode::asText);
	}

	@Override
	public boolean hasCollection(ModuleEntry collection) {
		String name = collection.name();
		String formattedName = name.toLowerCase(Locale.ENGLISH);
		JsonNode node = root.get(formattedName);
		return null != node && !node.isEmpty();
	}

	@Override
	public boolean hasList(ModuleList list) {
		String name = list.name();
		String formattedName = name.toLowerCase(Locale.ENGLISH);
		JsonNode node = root.get(formattedName);
		return null != node && !node.isEmpty();
	}
}
