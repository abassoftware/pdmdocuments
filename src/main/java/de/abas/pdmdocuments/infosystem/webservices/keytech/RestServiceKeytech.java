package de.abas.pdmdocuments.infosystem.webservices.keytech;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.abas.pdmdocuments.infosystem.PdmDocumentsException;
import de.abas.pdmdocuments.infosystem.config.Configuration;
import de.abas.pdmdocuments.infosystem.data.PdmDocument;
import de.abas.pdmdocuments.infosystem.utils.Util;
import de.abas.pdmdocuments.infosystem.webservices.AbstractRestService;

public class RestServiceKeytech extends AbstractRestService {

	private static final String PDM_DOCUMENT_RESTSERVICE_KEYTECH_ERROR_JSON_TO_OBJECT = "pdmDocument.restservice.keytech.error.jsonToObject";
	private static final String SEARCHPRODUCT_URL = "%1s/Search?classtypes=DEFAULT_MI&fields=as_mi__name=%2s";
	private static final String LINKEDDOCUMENT_URL = "%1s/elements/%2s/links";
	private static final String FILES_AT_DOKUMENT_URL = "%1s/elements/%2s/files";
	private static final String GETFILE_URL = "%1s/elements/%2s/files/%3s";

	public RestServiceKeytech(String server, String user, String password) {
		super();
		this.setServer(server);
		this.setUser(user);
		this.setPasword(password);
	}

	public RestServiceKeytech(Configuration config) {
		super();
		this.setServer(config.getRestServer());
		this.setUser(config.getRestUser());
		this.setPasword(config.getRestPassword());
	}

	public String searchPdmProductID(String abasIdNo) throws PdmDocumentsException {

		String url = String.format(SEARCHPRODUCT_URL, this.server, abasIdNo);

		String jsonString = callRestservice(url);

		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		ResponsePDMProductId response;
		try {
			response = mapper.readValue(jsonString, ResponsePDMProductId.class);
		} catch (IOException e) {
			throw new PdmDocumentsException(
					Util.getMessage(PDM_DOCUMENT_RESTSERVICE_KEYTECH_ERROR_JSON_TO_OBJECT, "ResponsePDMProductId"), e);
		}

		ArrayList<Elements> elementsList = response.getElementsList();
		if (elementsList.size() == 1) {
			return elementsList.get(0).getKey();
		} else if (elementsList.isEmpty()) {
			throw new PdmDocumentsException(Util.getMessage("pdmDocument.restservice.keytech.noResult", abasIdNo));
		} else {
			throw new PdmDocumentsException(
					Util.getMessage("pdmDocument.restservice.keytech.moreThanOneResult", abasIdNo));
		}

	}

	private PDMLinks searchProductLinks(String pdmProductID) throws PdmDocumentsException {

		String url = String.format(LINKEDDOCUMENT_URL, this.server, pdmProductID);
		String jsonString = callRestservice(url);

		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		PDMLinks pdmLinks;
		try {
			pdmLinks = mapper.readValue(jsonString, PDMLinks.class);
		} catch (IOException e) {
			throw new PdmDocumentsException(
					Util.getMessage(PDM_DOCUMENT_RESTSERVICE_KEYTECH_ERROR_JSON_TO_OBJECT, "PDMLink"), e);
		}

		return pdmLinks;

	}

	@Override
	public ArrayList<PdmDocument> getAllDocuments(String abasIdNo, String[] fileTypList) throws PdmDocumentsException {
		String pdmProductID = searchPdmProductID(abasIdNo);
		ArrayList<PdmDocument> pdmDocumentList = getDocumentsFromKeytech(pdmProductID);

		ArrayList<PdmDocument> filterpdmDocumentsList = filterPdmDocs(pdmDocumentList, fileTypList);

		getFilesforPDMDocs(filterpdmDocumentsList);

		return filterpdmDocumentsList;

	}

	private ArrayList<PdmDocument> getDocumentsFromKeytech(String pdmProductID) throws PdmDocumentsException {
		PDMLinks pdmLinks = searchProductLinks(pdmProductID);
		ArrayList<PdmDocument> pdmDocumentList = new ArrayList<>();

		ArrayList<ChildLink> elementChildLinks = pdmLinks.getElementChildLinks();

		for (ChildLink childLink : elementChildLinks) {

			List<FileInfo> fileInfoList = getFileInfoList(childLink);

			for (FileInfo fileInfo : fileInfoList) {

				String urlDocFile = String.format(GETFILE_URL, this.server, childLink.getChildLinkTo(),
						fileInfo.getFileid());

				PdmDocument pdmDocument = new PdmDocument(fileInfo.getFileDisplayname(), fileInfo.getStoretyp(),
						urlDocFile);
				pdmDocument.addDocMetaData("FileID", fileInfo.getFileid());
				pdmDocument.addDocMetaData("FileName", fileInfo.getFilename());
				pdmDocument.addDocMetaData("StoreTyp", fileInfo.getStoretyp());
				pdmDocument.addDocMetaData("FileSize", fileInfo.getFileSize());
				Map<String, Object> properties = fileInfo.getProperties();
				for (Map.Entry<String, Object> entry : properties.entrySet()) {
					Object value = entry.getValue();
					pdmDocument.addDocMetaData(entry.getKey(), value);
				}

				pdmDocumentList.add(pdmDocument);

			}

		}
		return pdmDocumentList;
	}

	private List<FileInfo> getFileInfoList(ChildLink childLink) throws PdmDocumentsException {

		String url = String.format(FILES_AT_DOKUMENT_URL, this.server, childLink.getChildLinkTo());

		String jsonString = callRestservice(url);

		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		FileInfoList response;
		try {
			response = mapper.readValue(jsonString, FileInfoList.class);
		} catch (IOException e) {
			log.error(e);
			throw new PdmDocumentsException(
					Util.getMessage(PDM_DOCUMENT_RESTSERVICE_KEYTECH_ERROR_JSON_TO_OBJECT, "FileInfoList"), e);
		}

		return response.getFileInfoList();

	}

	@Override
	public Boolean testConnection() {
		InputStream is = null;
		try {
			URL url = new URL(this.server);
			URLConnection con = url.openConnection();
			con.setConnectTimeout(TEST_TIMEOUT);
			is = con.getInputStream();
			log.info("Server erreichbar");
			return true;
		} catch (FileNotFoundException e) {
			log.info("Server erreichbar", e);
			// Server ist erreichbar, aber es ist keine richtige Seite
			// verfuegbar
			return true;
		} catch (IOException e) {
			log.error(e);
			return false;
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
				}
		}

	}

}
