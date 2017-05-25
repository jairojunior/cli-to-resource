package com.jjunior.wildfly.clitoresource.domain;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.dmr.Property;

public class ConfigurationResource {

	private String path;
	private Map<String, Object> attributes;
	private ModelNode originalNode;

	public ConfigurationResource(ModelNode node) {
		this.originalNode = node.clone();
		attributes = new TreeMap<>();
		node.asList().forEach(item -> process(item));
	}

	private void process(ModelNode item) {
		Property property = item.asProperty();

		if (property.getName().equals("address")) {
			this.path = property.getValue().asList().stream().map(address -> toPath(address)).reduce("",
					(path, nextPath) -> path + nextPath);

		} else if (property.getName().equals("operation")) {

		} else {
			this.attributes.put(property.getName(), transform(property.getValue()));
		}
	}

	private String transform(ModelNode value) {
		if (value.getType().equals(ModelType.STRING)) {
			return "'" + value.asString() + "'";
		} else if (value.getType().equals(ModelType.OBJECT)) {
			return value.asString().replaceAll("\"", "'");
		} else {
			return value.asString();
		}
	}

	private String toPath(ModelNode address) {
		Property property = address.asProperty();
		return String.format("/%s=%s", property.getName(), property.getValue().asString());
	}

	public String getPath() {
		return path;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void merge(ConfigurationResource childConfigurationResource) {
		List<ModelNode> childAddress = childConfigurationResource.getOriginalNode().get("address").asList();
		
		Property relativeAddress = childAddress.get(childAddress.size() - 1).asProperty();
		
		ModelNode subResourceAttributes = new ModelNode(ModelType.LIST);
		subResourceAttributes.setEmptyList();
		
		for (Entry<String, Object> entry : childConfigurationResource.getAttributes().entrySet()) {
			subResourceAttributes.add(entry.getKey(), entry.getValue().toString().replaceAll("'", ""));
		}

		ModelNode subResource = new ModelNode(ModelType.OBJECT);
		subResource.set(relativeAddress.getValue().asString(), subResourceAttributes.asObject());
		
		attributes.put(relativeAddress.getName(), transform(subResource.asObject()));
	}

	public ModelNode getOriginalNode() {
		return originalNode;
	}

}
