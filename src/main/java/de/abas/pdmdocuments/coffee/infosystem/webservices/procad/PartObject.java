package de.abas.pdmdocuments.coffee.infosystem.webservices.procad;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "Values", "Header" })
public class PartObject {

	@JsonProperty("Values")
	private Values values;

	@JsonIgnore
	private Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

	@JsonProperty("Values")
	public Values getValues() {
		return this.values;
	}

	@JsonProperty("Values")
	public void setValues(Values values) {
		this.values = values;
	}

	@JsonAnyGetter
	public Map<String, java.lang.Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, java.lang.Object value) {
		this.additionalProperties.put(name, value);
	}

}
