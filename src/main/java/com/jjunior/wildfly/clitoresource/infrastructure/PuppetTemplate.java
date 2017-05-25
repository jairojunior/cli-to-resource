package com.jjunior.wildfly.clitoresource.infrastructure;

import java.io.StringWriter;
import java.io.Writer;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.jjunior.wildfly.clitoresource.domain.ConfigurationResource;

public class PuppetTemplate {

	private ConfigurationResource configurationResource;

	public PuppetTemplate(ConfigurationResource configurationResource) {
		this.configurationResource = configurationResource;
	}

	public String render() {
		VelocityContext velocityContext = new VelocityContext();

		velocityContext.put("path", configurationResource.getPath());
		velocityContext.put("attributes", configurationResource.getAttributes().entrySet());

		Template template = Velocity.getTemplate("puppet.vm");

		Writer stringWriter = new StringWriter();

		template.merge(velocityContext, stringWriter);

		return stringWriter.toString();
	}

}
