package com.jjunior.wildfly.clitoresource.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandFormatException;
import org.jboss.dmr.ModelNode;

import com.jjunior.wildfly.clitoresource.domain.ConfigurationResource;
import com.jjunior.wildfly.clitoresource.domain.ModelNodeBuilder;
import com.jjunior.wildfly.clitoresource.infrastructure.PuppetTemplate;

@Path("/cli")
public class CliEndpoint {

	private static final String APPLICATION_VND_WILDFLY_RESOURCE_PUPPET_JSON = "application/vnd.wildfly.resource.puppet+json";

	@Inject
	private CommandContext commandContext;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ APPLICATION_VND_WILDFLY_RESOURCE_PUPPET_JSON })
	public Response doGet(CliRequest request) {
		ConfigurationResource rootConfigurationResource = null;
		Response response = null;

		CliResponse cliResponse = new CliResponse();

		try {

			if (request.isRecursive()) {

				ModelNode rootNode = new ModelNodeBuilder(commandContext).from(request.getRootCommand()).build();
				rootConfigurationResource = new ConfigurationResource(rootNode);

				for (String command : request.getChildrenCommands()) {
					ModelNode childNode = new ModelNodeBuilder(commandContext).from(command).build();
					ConfigurationResource childConfigurationResource = new ConfigurationResource(childNode);

					rootConfigurationResource.merge(childConfigurationResource);
				}

			} else {
				for (String command : request.getCommands()) {
					ModelNode node = new ModelNodeBuilder(commandContext).from(command).build();
					rootConfigurationResource = new ConfigurationResource(node);
				}
			}

			cliResponse.setContent(new PuppetTemplate(rootConfigurationResource).render());
			response = Response.ok().type(APPLICATION_VND_WILDFLY_RESOURCE_PUPPET_JSON).entity(cliResponse).build();

		} catch (CommandFormatException cfe) {
			cliResponse.setError(cfe.getMessage());
			response = Response.status(Status.BAD_REQUEST).entity(cliResponse).build();
		}

		return response;
	}
}