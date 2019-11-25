package de.abas.pdmdocuments.infosystem.webservices.procad;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.abas.pdmdocuments.infosystem.PdmDocumentsException;
import de.abas.pdmdocuments.infosystem.config.Configuration;
import de.abas.pdmdocuments.infosystem.data.PdmDocument;
import de.abas.pdmdocuments.infosystem.utils.UtilwithAbasConnection;
import de.abas.pdmdocuments.infosystem.webservices.AbstractRestService;

public class RestServiceProcad extends AbstractRestService {

	private static final String SERVER_ERREICHBAR = "Server erreichbar";
	private static final String TESTSERVICE_URL = "http://%1s/procad/profile/api/version";

	// Auf der URL https://{server}:{port}/procad/profile/api/{tenant}/ wird der
	// Rest-Servie von ProFile aufgerufen.

	private static final String BASE_URL = "http://%1s/procad/profile/api/%2s/";
	private static final String SEARCHPRODUCT_URL = "objects/Part?query='%3s'='%4s'";
	private static final String GETDOCUMENT_INFO = "objects/Document/%3s/";
	private static final String GETDOCUMENT_FILE = "objects/Document/%3s/file";
	private String tenant;

	private Configuration config;

	public RestServiceProcad(Configuration config) {
		super();
		this.config = config;
		this.setServer(config.getRestServer());
		this.setUser(config.getRestUser());
		this.setPasword(config.getRestPassword());
		this.tenant = config.getRestTenant();

		String testRestServiceUrl = String.format(TESTSERVICE_URL, this.server);
		testRestService(testRestServiceUrl);
	}

	protected String searchPdmProductID(String abasIdNo) throws PdmDocumentsException {

		String url = String.format(BASE_URL + SEARCHPRODUCT_URL, this.server, this.tenant,
				this.config.getPartFieldName(), abasIdNo);

		String jsonString = callRestservice(url);
		if (jsonString.equals("404")) {
			throw new PdmDocumentsException(UtilwithAbasConnection
					.getMessage("pdmDocument.restservice.procad.error.ProductID.notfound", "abasidno"));
		}

		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		ResponsePDMProductId response;
		try {
			response = mapper.readValue(jsonString, ResponsePDMProductId.class);
		} catch (IOException e) {
			throw new PdmDocumentsException(UtilwithAbasConnection
					.getMessage("pdmDocument.restservice.procad.error.jsonToObject", "ResponsePDMProductId"), e);
		}

		List<PartObject> elementsList = response.getObjectsList();
		if (elementsList.size() == 1) {

			Values values = elementsList.get(0).getValues();
			Map<String, Object> prop = values.getAdditionalProperties();

			if (prop.containsKey(config.getPartFieldName())) {
				Object objektID = prop.get(config.getPartProFileIDFieldName());
				if (objektID instanceof String) {
					return (String) objektID;

				}
				if (objektID instanceof Integer) {
					Integer objIntegerID = (Integer) objektID;
					return objIntegerID.toString();

				}
			}
			throw new PdmDocumentsException(
					UtilwithAbasConnection.getMessage("pdmDocument.restservice.procad.ResultFalseField", abasIdNo));
		} else if (elementsList.isEmpty()) {
			throw new PdmDocumentsException(
					UtilwithAbasConnection.getMessage("pdmDocument.restservice.procad.noResult", abasIdNo));
		} else {
			throw new PdmDocumentsException(
					UtilwithAbasConnection.getMessage("pdmDocument.restservice.procad.moreThanOneResult", abasIdNo));
		}

	}

	@Override
	public List<PdmDocument> getAllDocuments(String abasIdNo, String[] fileTypList) throws PdmDocumentsException {
		String pdmProductID = searchPdmProductID(abasIdNo);
		List<String> procadDocuments = getDokumentsFromSQL(pdmProductID);
		List<PdmDocument> pdmDocs = getPdmDocumentsfromProcad(procadDocuments);
		List<PdmDocument> filterpdmDocumentsList = filterPdmDocs(pdmDocs, fileTypList);
		getFilesforPDMDocs(filterpdmDocumentsList);
		return filterpdmDocumentsList;
	}

	private List<PdmDocument> getPdmDocumentsfromProcad(List<String> procadDocuments) throws PdmDocumentsException {
		List<PdmDocument> pdmDocs = new ArrayList<>();
		ProcadDocument response;

		for (String proString : procadDocuments) {
			String urlDocInfo = String.format(BASE_URL + GETDOCUMENT_INFO, this.server, this.tenant, proString);
			String urlDocFile = String.format(BASE_URL + GETDOCUMENT_FILE, this.server, this.tenant, proString);
			log.info(UtilwithAbasConnection.getMessage("pdmDocument.restservice.procad.searchDokid", proString));
			String jsonString = callRestservice(urlDocInfo);

			if (!jsonString.equals("404")) {
				ObjectMapper mapper = new ObjectMapper();
				mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
				try {
					response = mapper.readValue(jsonString, ProcadDocument.class);
					if (response.getHeader().getHasFile()) {

						Values values = response.getValues();
						Map<String, Object> valueMap = values.getAdditionalProperties();

						String filename = getStringFromMap(valueMap, config.getDocVersionBaseIDFieldName()) + "_"
								+ getStringFromMap(valueMap, config.getOrgNameFieldName());

						String docType = getStringFromMap(valueMap, config.getDocTypeFieldName());
						PdmDocument pdmDocument = new PdmDocument(filename, docType, urlDocFile);

						Set<String> keyset = valueMap.keySet();
						for (String key : keyset) {
							pdmDocument.addDocMetaData(key, valueMap.get(key));
						}
						pdmDocs.add(pdmDocument);

					} else {
						log.error(UtilwithAbasConnection
								.getMessage("pdmDocument.restservice.procad.error.noFiletoDocID", proString));
					}

				} catch (IOException e) {
					throw new PdmDocumentsException(UtilwithAbasConnection.getMessage(
							"pdmDocument.restservice.procad.error.jsonToObject", "ResponsePDMProductId"), e);
				}
			} else {
				log.error(UtilwithAbasConnection.getMessage("pdmDocument.restservice.procad.error.Dokument.notfound",
						proString));
			}
		}

		return pdmDocs;
	}

	private String getStringFromMap(Map<String, Object> valueMap, String searchString) throws PdmDocumentsException {
		Object fileNameObj = valueMap.get(searchString);
		String filename;
		if (fileNameObj != null) {
			if (fileNameObj instanceof String) {
				filename = (String) fileNameObj;

			} else {
				filename = fileNameObj.toString();
			}
			return filename;
		}
		throw new PdmDocumentsException(
				UtilwithAbasConnection.getMessage("pdmDocument.restservice.profile.error.valueNotinMap", searchString));
	}

	private List<String> getDokumentsFromSQL(String pdmProductID) throws PdmDocumentsException {

		SQLConnectionHandler sqlConnHandler = new SQLConnectionHandler(config);

		String sqlQuery = "select vb_objidnr2 from " + config.getSqldatabase() + ".dbo.VERBIND where vb_objidnr1 ="
				+ pdmProductID + " and vb_objtyp1 =2 and vb_objtyp2=3";
		return sqlConnHandler.executeQuery(sqlQuery);

	}

	public boolean testRestService(String urlString) {

		InputStream is = null;
		try {
			URL url = new URL(urlString);
			URLConnection con = url.openConnection();

			is = con.getInputStream();
			log.info(SERVER_ERREICHBAR);
			return true;
		} catch (FileNotFoundException e) {
			log.info(SERVER_ERREICHBAR, e);
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
					log.error(e);
				}
		}

	}

	@Override
	public Boolean testConnection() throws PdmDocumentsException {
		return (testRestServer() && testSQLServer());

	}

	private boolean testSQLServer() {

		try {
			new SQLConnectionHandler(config);
		} catch (Exception e) {
			log.error(e);
			return false;
		}
		return true;

	}

	private Boolean testRestServer() {
		String testRestServiceUrl = String.format(TESTSERVICE_URL, this.server);
		InputStream is = null;
		try {
			URL url = new URL(testRestServiceUrl);
			URLConnection con = url.openConnection();
			con.setConnectTimeout(TEST_TIMEOUT);

			is = con.getInputStream();
			log.info(SERVER_ERREICHBAR);
			return true;
		} catch (FileNotFoundException e) {
			log.info(SERVER_ERREICHBAR, e);
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
					log.error(e);
				}
		}
	}

}
