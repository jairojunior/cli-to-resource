package com.jjunior.wildfly.clitoresource.domain;

import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandFormatException;
import org.jboss.dmr.ModelNode;

public class ModelNodeBuilder {
	
	private ModelNode modelNode;
	private CommandContext context;
	
	public ModelNodeBuilder(CommandContext context) {
		this.context = context;
	}
	
	public ModelNodeBuilder from(String command) throws CommandFormatException {
		this.modelNode = context.buildRequest(command);
		return this;
	}
	
	public ModelNode build() {
		return modelNode;
	}

}
