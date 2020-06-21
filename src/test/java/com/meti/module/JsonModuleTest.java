package com.meti.module;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static com.meti.module.ModuleEntry.CONTENT;
import static com.meti.module.ModuleList.INSTALL;
import static com.meti.module.ModuleProperty.*;
import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.*;

class JsonModuleTest {

	private final ObjectMapper mapper = new ObjectMapper();

	@Test
	void getCollection() {
		JsonNode item = mapper.createObjectNode()
				.put("type", "url")
				.put("value", "https://www.google.com/");
		JsonNode content = mapper.createArrayNode().add(item);
		JsonNode root = mapper.createObjectNode().set("content", content);
		Module module = new JsonModule(root);
		Collection<Map<String, String>> collection = module.getCollection(CONTENT);
		assertEquals(1, collection.size());
		Map<?, ?> map = (Map<?, ?>) collection.toArray()[0];
		assertEquals(2, map.size());
		assertEquals("url", map.get("type"));
		assertEquals("https://www.google.com/", map.get("value"));
	}

	@Test
	void getList() {
		JsonNode array = mapper.createArrayNode().add("test");
		JsonNode node = mapper.createObjectNode().set("install", array);
		Module module = new JsonModule(node);
		Collection<String> list = module.getList(INSTALL);
		assertEquals(1, list.size());
		assertIterableEquals(singleton("test"), list);
	}

	@Test
	void getProperty() {
		JsonNode node = mapper.createObjectNode().put("name", "test");
		Module module = new JsonModule(node);
		Optional<String> optional = module.getProperty(NAME);
		assertTrue(optional.isPresent());
		assertEquals("test", optional.get());
	}

	@Test
	void hasCollection() {
		JsonNode item = mapper.createObjectNode()
				.put("type", "url")
				.put("value", "https://www.google.com/");
		JsonNode content = mapper.createArrayNode().add(item);
		JsonNode root = mapper.createObjectNode().set("content", content);
		Module module = new JsonModule(root);
		assertTrue(module.hasCollection(CONTENT));
	}

	@Test
	void hasList() {
		JsonNode array = mapper.createArrayNode().add("test");
		JsonNode node = mapper.createObjectNode().set("install", array);
		Module module = new JsonModule(node);
		assertTrue(module.hasList(INSTALL));
	}
}