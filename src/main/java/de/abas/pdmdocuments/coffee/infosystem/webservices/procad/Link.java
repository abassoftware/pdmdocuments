
package de.abas.pdmdocuments.coffee.infosystem.webservices.procad;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "Href", "Type" })
public class Link {

	@JsonProperty("Href")
	private String href;
	@JsonProperty("Type")
	private String type;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("Href")
	public String getHref() {
		return href;
	}

	@JsonProperty("Href")
	public void setHref(String href) {
		this.href = href;
	}

	@JsonProperty("Type")
	public String getType() {
		return type;
	}

	@JsonProperty("Type")
	public void setType(String type) {
		this.type = type;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
