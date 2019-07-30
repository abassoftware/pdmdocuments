package de.abas.pdmdocuments.infosystem.webservices.keytech;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author tkellermann
 *
 */
/**
 * @author tkellermann
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileInfo {

	@JsonProperty("FileID")
	private String fileid;

	@JsonProperty("FileName")
	private String filename;

	@JsonProperty("FileDisplayname")
	private String fileDisplayname;

	@JsonProperty("FileSize")
	private Integer fileSize;

	@JsonProperty("FileStorageType")
	private String storetyp;

	private Map<String, Object> properties = new HashMap<>();

	public String getFileid() {
		return fileid;
	}

	public String getFilename() {
		return filename;
	}

	public Integer getFileSize() {
		return fileSize;
	}

	public String getStoretyp() {
		return storetyp;
	}

	public String getFileDisplayname() {
		return fileDisplayname;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	@JsonAnySetter
	public void add(String key, Object value) {
		properties.put(key, value);
	}

	/**
	 * In dem Elementkey ist in dem Teil vor dem ":" der Dokumenttyp enthalten
	 * 
	 * @return documenttyp
	 */
	public String getDocumenttyp() {

		String value = (String) this.properties.get("ElementKey");
		String[] splitvalue = value.split(":");
		return splitvalue[0];
	}

}
