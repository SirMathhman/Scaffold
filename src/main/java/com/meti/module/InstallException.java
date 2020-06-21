package com.meti.module;

public class InstallException extends Exception {
	public InstallException(String message, Throwable cause) {
		super(message, cause);
	}

	public InstallException(String message) {
		super(message);
	}
}
