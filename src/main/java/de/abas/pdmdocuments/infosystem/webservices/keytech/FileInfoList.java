package de.abas.pdmdocuments.infosystem.webservices.keytech;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FileInfoList {

	@JsonProperty("FileInfos")
	private ArrayList<FileInfo> fileInfos;

	public List<FileInfo> getFileInfoList() {
		return fileInfos;
	}

}
