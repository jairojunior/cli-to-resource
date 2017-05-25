package com.jjunior.wildfly.clitoresource.infrastructure;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;

import org.jboss.as.cli.CliInitializationException;
import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.impl.CommandContextFactoryImpl;

@RequestScoped
public class CommandContextProducer {
	
	@Produces
	public CommandContext commandContext() throws CliInitializationException {
		CommandContextFactoryImpl commandFactory = new CommandContextFactoryImpl();
		
		return commandFactory.newCommandContext();
	}

}
