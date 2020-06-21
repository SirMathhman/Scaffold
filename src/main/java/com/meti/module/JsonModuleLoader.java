package com.meti.module;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class JsonModuleLoader implements ModuleLoader {
	private final ObjectMapper mapper;

	public JsonModuleLoader() {
		this(new ObjectMapper());
	}

	public JsonModuleLoader(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public Module load(InputStream resource) throws IOException {
		return new JsonModule(mapper.readTree(resource));
	}
}