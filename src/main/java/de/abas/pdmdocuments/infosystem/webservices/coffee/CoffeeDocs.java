package de.abas.pdmdocuments.infosystem.webservices.coffee;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CoffeeDocs {

	@JsonProperty("FileID")
	private String fileID;

	@JsonProperty("Extension")
	private String extension;

	@JsonProperty("FileName")
	private String fileName;

	@JsonProperty("FilePath")
	private String filePath;

	@JsonProperty("LocalVersion")
	private String localVersion;

	@JsonProperty("LatestVersion")
	private String latestVersion;

	@JsonProperty("CheckedIn")
	private Boolean checkedIn;

	@JsonProperty("CheckedInBy")
	private String checkedInBy;

	@JsonProperty("CheckedInDate")
	private String checkedInDate;

	public String getFileID() {
		return fileID;
	}

	public String getExtension() {
		return extension;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getLocalVersion() {
		return localVersion;
	}

	public String getLatestVersion() {
		return latestVersion;
	}

	public Boolean getCheckedIn() {
		return checkedIn;
	}

	public String getCheckedInBy() {
		return checkedInBy;
	}

	public String getCheckedInDate() {
		return checkedInDate;
	}

}
