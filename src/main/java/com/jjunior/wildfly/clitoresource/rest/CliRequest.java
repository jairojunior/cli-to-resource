package com.jjunior.wildfly.clitoresource.rest;

import java.util.List;

public class CliRequest {

	private List<String> commands;
	private boolean recursive;
	
	public CliRequest(List<String> commands, boolean recursive) {
		this.commands = commands;
		this.recursive = recursive;
	}
	
	public CliRequest() {
	}

	public List<String> getCommands() {
		return commands;
	}

	public boolean isRecursive() {
		return recursive;
	}

	public String getRootCommand() {
		return commands.get(0);
	}

	public List<String> getChildrenCommands() {
		return commands.subList(1, commands.size());
	}

}
