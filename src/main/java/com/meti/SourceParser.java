package com.meti;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

public interface SourceParser {
	Optional<Source> parse(JsonNode value);
}
