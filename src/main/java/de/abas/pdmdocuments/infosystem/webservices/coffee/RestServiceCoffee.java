package de.abas.pdmdocuments.infosystem.webservices.coffee;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.abas.pdmdocuments.infosystem.PdmDocumentsException;
import de.abas.pdmdocuments.infosystem.config.Configuration;
import de.abas.pdmdocuments.infosystem.data.PdmDocument;
import de.abas.pdmdocuments.infosystem.utils.Util;
import de.abas.pdmdocuments.infosystem.webservices.AbstractRestService;

public class RestServiceCoffee extends AbstractRestService {

	private static final String PDM_DOCUMENT_RESTSERVICE_COFFEE_ERROR_JSON_TO_OBJECT = "pdmDocument.restservice.keytech.error.jsonToObject";

	private static final String GETFILE_URL_PART1 = "http://%1s/download?json=";
	private static final String GETFILE_URL_PART2 = "{\"FileId\":%1s,\"Version\":%2s}";

	private static final String GETCOFFEEDOCS_URL_PART1 = "http://%1s/listing?json=";
	private static final String GETCOFFEEDOCS_URL_PART2 = "{\"Extension\":\"\",\"Variables\":[{\"Name\":\"ABAS_Identnummer\",\"Option\":106,\"Value\":\"%2s\"}],\"FileId\":0,\"Version\":0}";

	public RestServiceCoffee(String server, String user, String password) {
		super();
		this.setServer(server);
		this.setUser(user);
		this.setPasword(password);
	}

	public RestServiceCoffee(Configuration config) {
		super();
		this.setServer(config.getRestServer());
		this.setUser(config.getRestUser());
		this.setPasword(config.getRestPassword());
	}

	@Override
	public ArrayList<PdmDocument> getAllDocuments(String abasIdNo, String[] fileTypList) throws PdmDocumentsException {

		ArrayList<PdmDocument> pdmDocumentList = getDocumentsFromCoffee(abasIdNo);

		ArrayList<PdmDocument> filterpdmDocumentsList = filterPdmDocs(pdmDocumentList, fileTypList);

		getFilesforPDMDocs(filterpdmDocumentsList);

		return filterpdmDocumentsList;

	}

	private ArrayList<PdmDocument> getDocumentsFromCoffee(String abasIdNo) throws PdmDocumentsException {
		List<CoffeeDocs> response;
		try {
			String urlPart1 = String.format(GETCOFFEEDOCS_URL_PART1, this.server);
			String urlPart2 = URLEncoder.encode(String.format(GETCOFFEEDOCS_URL_PART2, abasIdNo), "UTF-8");
			String url = urlPart1 + urlPart2;
			String jsonString = callRestservice(url);
			log.debug("json: \n" + jsonString);
			if (jsonString.contains("Status Code")) {
				throw new PdmDocumentsException(
						Util.getMessage("pdmDocument.restservice.procad.error.InternalServerError", jsonString));
			}
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			response = mapper.readValue(jsonString, new TypeReference<List<CoffeeDocs>>() {
			});

			List<CoffeeDocs> listCoffeeDocs = response;

			ArrayList<PdmDocument> pdmDocumentList = new ArrayList<>();

			for (CoffeeDocs coffedocs : listCoffeeDocs) {

				String urlDocFilePart1 = String.format(GETFILE_URL_PART1, this.server);

				String urlDocFilePart2 = URLEncoder.encode(
						String.format(GETFILE_URL_PART2, coffedocs.getFileID(), coffedocs.getLatestVersion()), "UTF-8");

				String urlDocFile = urlDocFilePart1 + urlDocFilePart2;

				PdmDocument pdmDocument = new PdmDocument(coffedocs.getFileName(), coffedocs.getExtension(),
						urlDocFile);

				pdmDocument.addDocMetaData("FileID", coffedocs.getFileID());
				pdmDocument.addDocMetaData("FileName", coffedocs.getFileName());
				pdmDocument.addDocMetaData("FilePath", coffedocs.getFilePath());
				pdmDocument.addDocMetaData("LocalVersion", coffedocs.getLocalVersion());
				pdmDocument.addDocMetaData("LatestVersion", coffedocs.getLatestVersion());
				pdmDocument.addDocMetaData("CheckedIn", coffedocs.getCheckedIn());
				pdmDocument.addDocMetaData("CheckedInBy", coffedocs.getCheckedInBy());
				pdmDocument.addDocMetaData("CheckedInDate", coffedocs.getCheckedInDate());

				pdmDocumentList.add(pdmDocument);

			}

			return pdmDocumentList;

		} catch (IOException e) {
			log.error(e);
			throw new PdmDocumentsException(
					Util.getMessage(PDM_DOCUMENT_RESTSERVICE_COFFEE_ERROR_JSON_TO_OBJECT, "CoffeeDocs"), e);
		}

	}

	@Override
	public Boolean testConnection() {
		InputStream is = null;
		try {
			URL url = new URL("http://" + this.server);
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
