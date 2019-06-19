package de.abas.pdmdocuments.coffee.infosystem.webservices.procad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "Objects" })
public class ResponsePDMProductId {

	@JsonProperty("Objects")
	private List<PartObject> partObjects = null;

	@JsonIgnore
	private Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

	@JsonProperty("Objects")
	public List<PartObject> getObjects() {
		return partObjects;
	}

	@JsonProperty("Objects")
	public void setObjects(List<PartObject> objects) {
		this.partObjects = objects;
	}

	public List<PartObject> getObjectsList() {
		return partObjects;
	}

	public void setObjectsList(ArrayList<PartObject> objectsList) {
		this.partObjects = objectsList;
	}

	public List<PartObject> getElementsList() {
		return partObjects;
	}

}
