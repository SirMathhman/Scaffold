package com.meti;

public class InstallException extends Exception {
	public InstallException(String message, Throwable cause) {
		super(message, cause);
	}

	public InstallException(String message) {
		super(message);
	}
}
