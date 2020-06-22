package com.meti;

import java.util.Collection;

public interface Module {
	String artifact();

	Collection<Module> children();

	Collection<Source> content();

	Collection<Source> dependencies();

	Collection<String> enter();

	Collection<String> exit();

	String group();

	Collection<Source> tasks();

	String version();
}
