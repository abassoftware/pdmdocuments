package de.abas.pdmdocuments.coffee.infosystem.webservices.procad;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.abas.pdmdocuments.coffee.infosystem.PdmDocumentsException;
import de.abas.pdmdocuments.coffee.infosystem.config.Configuration;
import de.abas.pdmdocuments.coffee.infosystem.data.PdmDocument;
import de.abas.pdmdocuments.coffee.infosystem.utils.Util;
import de.abas.pdmdocuments.coffee.infosystem.webservices.AbstractRestService;

public class RestServiceProcad extends AbstractRestService {

	private static String TESTSERVICE_URL = "http://%1s/procad/profile/api/version";
	// https://{server}:{port}/procad/profile/api/{tenant}/
	private static String BASE_URL = "http://%1s/procad/profile/api/%2s/";
	private static String SEARCHPRODUCT_URL = "objects/Part?query='%3s'='%4s'";
	private static String GETDOCUMENT_INFO = "objects/Document/%3s/";
	private static String GETDOCUMENT_FILE = "objects/Document/%3s/file";
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

	@Override
	public String searchPdmProductID(String abasIdNo) throws PdmDocumentsException {

		String url = String.format(BASE_URL + SEARCHPRODUCT_URL, this.server, this.tenant,
				this.config.getPartFieldName(), abasIdNo);

		String jsonString = callRestservice(url);
		if (jsonString.equals("404")) {
			throw new PdmDocumentsException(
					Util.getMessage("pdmDocument.restservice.procad.error.ProductID.notfound", "abasidno"));
		}

		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		ResponsePDMProductId response;
		try {
			response = mapper.readValue(jsonString, ResponsePDMProductId.class);
		} catch (JsonParseException e) {
			throw new PdmDocumentsException(
					Util.getMessage("pdmDocument.restservice.procad.error.jsonToObject", "ResponsePDMProductId"), e);
		} catch (JsonMappingException e) {
			throw new PdmDocumentsException(
					Util.getMessage("pdmDocument.restservice.procad.error.jsonToObject", "ResponsePDMProductId"), e);
		} catch (IOException e) {
			throw new PdmDocumentsException(
					Util.getMessage("pdmDocument.restservice.procad.error.jsonToObject", "ResponsePDMProductId"), e);
		}

		List<PartObject> elementsList = response.getObjectsList();
		if (elementsList.size() == 1) {
			// return elementsList.get(0).getKey();
			Values values = elementsList.get(0).getValues();
			Map<String, Object> prop = values.getAdditionalProperties();

			if (prop.containsKey(config.getPartFieldName())) {
				Object objektID = prop.get(config.getPartProFileIDFieldName());
				if (objektID instanceof String) {
					String objStringID = (String) objektID;
					return objStringID;
				}
				if (objektID instanceof Integer) {
					Integer objIntegerID = (Integer) objektID;
					String objStringID = objIntegerID.toString();
					return objStringID;
				}
			}
			throw new PdmDocumentsException(
					Util.getMessage("pdmDocument.restservice.procad.ResultFalseField", abasIdNo));
		} else if (elementsList.size() == 0) {
			throw new PdmDocumentsException(Util.getMessage("pdmDocument.restservice.procad.noResult", abasIdNo));
		} else {
			throw new PdmDocumentsException(
					Util.getMessage("pdmDocument.restservice.procad.moreThanOneResult", abasIdNo));
		}

	}

	@Override
	public ArrayList<PdmDocument> getAllDocuments(String abasIdNo, String[] fileTypList) throws PdmDocumentsException {
		String pdmProductID = searchPdmProductID(abasIdNo);
		ArrayList<String> procadDocuments = getDokumentsFromSQL(pdmProductID);
		ArrayList<PdmDocument> pdmDocs = getPdmDocumentsfromProcad(procadDocuments);
		ArrayList<PdmDocument> filterpdmDocumentsList = filterPdmDocs(pdmDocs, fileTypList);
		getFilesforPDMDocs(filterpdmDocumentsList);
		return filterpdmDocumentsList;
	}

	private ArrayList<PdmDocument> getPdmDocumentsfromProcad(ArrayList<String> procadDocuments)
			throws PdmDocumentsException {
		ArrayList<PdmDocument> pdmDocs = new ArrayList<PdmDocument>();
		ProcadDocument response;

		for (String proString : procadDocuments) {
			String testString = BASE_URL + GETDOCUMENT_INFO;
			String urlDocInfo = String.format(BASE_URL + GETDOCUMENT_INFO, this.server, this.tenant, proString);
			String urlDocFile = String.format(BASE_URL + GETDOCUMENT_FILE, this.server, this.tenant, proString);
			log.info(Util.getMessage("pdmDocument.restservice.procad.searchDokid", proString));
			String jsonString = callRestservice(urlDocInfo);

			if (!jsonString.equals("404")) {
				ObjectMapper mapper = new ObjectMapper();
				mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
				try {
					response = mapper.readValue(jsonString, ProcadDocument.class);
					if (response.getHeader().getHasFile()) {

						Values values = response.getValues();
						Map<String, Object> valueMap = values.getAdditionalProperties();
						String searchFileName = "/Document/orgName";
						String searchDocVersionBaseId = "/Document/docVersionBaseId";
						String filename = getStringFromMap(valueMap, searchDocVersionBaseId) + "_"
								+ getStringFromMap(valueMap, searchFileName);

						String searchDocType = "/Document/docType";
						String docType = getStringFromMap(valueMap, searchDocType);
						String searchfileSize = "/Document/fileSize";
						Integer fileSize = getIntegerFromMap(valueMap, searchfileSize);
						PdmDocument pdmDocument = new PdmDocument(filename, docType, urlDocFile);

						Set<String> keyset = valueMap.keySet();
						for (String key : keyset) {
							pdmDocument.addDocMetaData(key, valueMap.get(key));
						}
						pdmDocs.add(pdmDocument);

						//
					} else {
						log.error(Util.getMessage("pdmDocument.restservice.procad.error.noFiletoDocID", proString));
					}

				} catch (JsonParseException e) {
					throw new PdmDocumentsException(Util.getMessage("pdmDocument.restservice.procad.error.jsonToObject",
							"ResponsePDMProductId"), e);
				} catch (JsonMappingException e) {
					throw new PdmDocumentsException(Util.getMessage("pdmDocument.restservice.procad.error.jsonToObject",
							"ResponsePDMProductId"), e);
				} catch (IOException e) {
					throw new PdmDocumentsException(Util.getMessage("pdmDocument.restservice.procad.error.jsonToObject",
							"ResponsePDMProductId"), e);
				}
			} else {
				log4j.error(Util.getMessage("pdmDocument.restservice.procad.error.Dokument.notfound", proString));
			}
		}

		return pdmDocs;
	}

	private String getStringFromMap(Map<String, Object> valueMap, String searchString) {
		Object fileNameObj = valueMap.get(searchString);
		String filename;
		if (fileNameObj instanceof String) {
			filename = (String) fileNameObj;

		} else {
			filename = fileNameObj.toString();
		}
		return filename;
	}

	private Integer getIntegerFromMap(Map<String, Object> valueMap, String searchString) {
		Object value = valueMap.get(searchString);
		Integer intValue = null;
		if (value instanceof Integer) {
			intValue = (Integer) value;

		}
		return intValue;
	}

	private ArrayList<String> getDokumentsFromSQL(String pdmProductID) throws PdmDocumentsException {

		SQLConnectionHandler sqlConnHandler = new SQLConnectionHandler(config);

		String sqlQuery = "select vb_objidnr2 from " + config.getSqldatabase() + ".dbo.VERBIND where vb_objidnr1 ="
				+ pdmProductID + " and vb_objtyp1 =2 and vb_objtyp2=3";
		ArrayList<String> result = sqlConnHandler.executeQuery(sqlQuery);

		return result;
	}

	public boolean testRestService(String urlString) {

		InputStream is = null;
		try {
			URL url = new URL(urlString);
			URLConnection con = url.openConnection();

			is = con.getInputStream();
			log.info("Server erreichbar");
			return true;
		} catch (NoRouteToHostException e) {
			log.error(e);
			return false;
		} catch (FileNotFoundException e) {
			log.info("Server erreichbar", e);
			// Server ist erreichbar, aber es ist keine richtige Seite
			// verfuegbar
			return true;
		} catch (MalformedURLException e) {
			log.error(e);
			return false;
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

	@Override
	public Boolean testConnection() throws PdmDocumentsException {
		if (testRestServer() && testSQLServer()) {
			return true;
		}

		return false;

	}

	private boolean testSQLServer() throws PdmDocumentsException {

		SQLConnectionHandler sqlConn = new SQLConnectionHandler(config);
		if (sqlConn != null) {
			return true;
		}
		return false;

	}

	private Boolean testRestServer() {
		String testRestServiceUrl = String.format(TESTSERVICE_URL, this.server);
		InputStream is = null;
		try {
			URL url = new URL(testRestServiceUrl);
			URLConnection con = url.openConnection();
			con.setConnectTimeout(TEST_TIMEOUT);

			is = con.getInputStream();
			log.info("Server erreichbar");
			return true;
		} catch (NoRouteToHostException e) {
			log.error(e);
			return false;
		} catch (FileNotFoundException e) {
			log.info("Server erreichbar", e);
			// Server ist erreichbar, aber es ist keine richtige Seite
			// verfuegbar
			return true;
		} catch (MalformedURLException e) {
			log.error(e);
			return false;
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
