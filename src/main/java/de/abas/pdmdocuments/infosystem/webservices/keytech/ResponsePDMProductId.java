package de.abas.pdmdocuments.infosystem.webservices.keytech;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponsePDMProductId {

	@JsonProperty("PageNumber")
	Integer pageNumber;

	@JsonProperty("Totalrecords")
	Integer totalRecords;

	@JsonProperty("PageSize")
	Integer pageSize;

	@JsonProperty("ElementList")
	ArrayList<Elements> elementsList;

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public ArrayList<Elements> getElementsList() {
		return elementsList;
	}

	// public void setElementsList(ArrayList<Elements> elementsList) {
//		this.elementsList = elementsList;
//	}

}
