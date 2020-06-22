package com.meti;

import java.io.OutputStream;

public interface Source {
	long transferTo(OutputStream out);
}
