
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
@JsonPropertyOrder({ "Values", "Header" })
public class ProcadDocument {

	@JsonProperty("Values")
	private Values values;
	@JsonProperty("Header")
	private Header header;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("Values")
	public Values getValues() {
		return values;
	}

	@JsonProperty("Values")
	public void setValues(Values values) {
		this.values = values;
	}

	@JsonProperty("Header")
	public Header getHeader() {
		return header;
	}

	@JsonProperty("Header")
	public void setHeader(Header header) {
		this.header = header;
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
