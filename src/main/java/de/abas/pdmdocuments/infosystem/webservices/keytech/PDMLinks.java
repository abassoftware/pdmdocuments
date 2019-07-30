package de.abas.pdmdocuments.infosystem.webservices.keytech;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PDMLinks {

	@JsonProperty("ElementChildLinks")
	private ArrayList<ChildLink> elementChildLinks;

	public ArrayList<ChildLink> getElementChildLinks() {
		return elementChildLinks;
	}

	public void setElementChildLinks(ArrayList<ChildLink> elementChildLinks) {
		this.elementChildLinks = elementChildLinks;
	}

}
