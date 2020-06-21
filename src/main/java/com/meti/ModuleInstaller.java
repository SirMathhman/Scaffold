package com.meti;

public interface ModuleInstaller {
	String install(Module module, ModuleLoader source) throws InstallException;
}
