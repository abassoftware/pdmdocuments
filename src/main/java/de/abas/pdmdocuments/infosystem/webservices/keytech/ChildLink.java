package de.abas.pdmdocuments.infosystem.webservices.keytech;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChildLink {

	@JsonProperty("ChildLinkTo")
	String childLinkTo;

	public String getChildLinkTo() {
		return childLinkTo;
	}

	public void setChildLinkTo(String childLinkTo) {
		this.childLinkTo = childLinkTo;
	}

}
