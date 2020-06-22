package com.meti;

import com.fasterxml.jackson.databind.JsonNode;

public interface StreamParser {
	Source parse(JsonNode value);
}
