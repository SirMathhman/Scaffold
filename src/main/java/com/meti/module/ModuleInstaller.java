package com.meti.module;

public interface ModuleInstaller {
	String install(Module module, ModuleLoader source) throws InstallException;
}
