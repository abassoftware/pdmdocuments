package de.abas.pdmdocuments.infosystem.webservices;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.ProcessingException;

import org.apache.log4j.Logger;
import org.jboss.resteasy.client.jaxrs.BasicAuthentication;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import de.abas.pdmdocuments.infosystem.DocumentsInterface;
import de.abas.pdmdocuments.infosystem.PdmDocumentsException;
import de.abas.pdmdocuments.infosystem.data.PdmDocument;
import de.abas.pdmdocuments.infosystem.utils.Util;
import de.abas.pdmdocuments.infosystem.utils.UtilwithAbasConnection;

public abstract class AbstractRestService implements DocumentsInterface {

	protected static final Logger log = Logger.getLogger(AbstractRestService.class);

	private static final String TIMESTAMP = Util.getTimestamp();
	protected static final int TEST_TIMEOUT = 5000;

	protected String server;
	protected String user;
	protected String password;

	@Override
	public void setPasword(String password) {
		this.password = password;

	}

	@Override
	public void setUser(String user) {
		this.user = user;

	}

	@Override
	public void setServer(String serveradresse) {
		this.server = serveradresse;

	}

	protected String callRestservice(String url) throws PdmDocumentsException {

		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target(url);
		target.register(new BasicAuthentication(this.user, this.password));
		log.info("call url - " + url);
		javax.ws.rs.core.Response response = target.request().get();
		if (response.getStatus() == 200) {
			log.info("ok - " + url);

			String value;
			try {
				value = response.readEntity(String.class);
				log.debug("Response : " + value);
			} catch (ProcessingException e) {
				log.error(e.getMessage());
				throw new PdmDocumentsException("Prozessfehler :" + e.getMessage());
			} catch (IllegalStateException e) {
				log.error(e.getMessage());
				throw new PdmDocumentsException("IllegalState-Fehler :" + e.getMessage());
			} finally {
				response.close();
			}
			return value;
		} else {
			if (response.getStatus() == 404) {
				return "404";
			}
			log.error(url + " " + response.getStatus() + " " + response.getStatusInfo() + " "
					+ response.getMetadata().toString());
			throw new PdmDocumentsException(
					UtilwithAbasConnection.getMessage("pdmDocument.restservice.keytech.error.getfilehttprequest",
							response.getStatus() + " " + response.getMetadata().toString()));
		}

	}

	protected List<File> downloadFileFromRestservice(String url, String fileName, String path) throws IOException {

		List<File> fileList = new ArrayList<>();

		ResteasyProviderFactory factory = ResteasyProviderFactory.getInstance();

		ResteasyClient client = null;

		ResteasyClientBuilder resteasyClientBuilder = new ResteasyClientBuilder().providerFactory(factory);

		client = resteasyClientBuilder.socketTimeout(20, TimeUnit.SECONDS).build();

		ResteasyWebTarget target = client.target(url);
		target.register(new BasicAuthentication(this.user, this.password));
		log.info(UtilwithAbasConnection.getMessage("pdmDocument.restservice.downloadFile.info", fileName, url));

		File outputfile = new File(path + fileName);

		try (OutputStream out = new FileOutputStream(outputfile);
				InputStream inputStream = target.request().get(InputStream.class);) {

			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = inputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

		}

		log.info(UtilwithAbasConnection.getMessage("pdmDocument.restservice.downloadFile.sucess", fileName));
		fileList.add(outputfile);

		return fileList;

	}

	protected void getFilesforPDMDocs(List<PdmDocument> pdmDocs) {

		for (PdmDocument pdmDocument2 : pdmDocs) {

			String targetFileName = Util.replaceUmlaute(pdmDocument2.getFilename().replaceAll(" ", "_"));
			try {

				List<File> fileList = downloadFileFromRestservice(pdmDocument2.getUrlDocFile(), targetFileName,
						getTargetPath());
				if (!fileList.isEmpty()) {
					File file = fileList.get(0);
					pdmDocument2.addFile(file);
				}
			} catch (SocketTimeoutException e) {
				log.error(UtilwithAbasConnection.getMessage("pdmDocument.restservice.procad.error.FileTimeOut",
						pdmDocument2.getUrlDocFile(), e.getMessage()));
				pdmDocument2
						.addError(UtilwithAbasConnection.getMessage("pdmDocument.restservice.procad.error.FileTimeOut",
								pdmDocument2.getUrlDocFile(), e.getMessage()));
			} catch (IOException e) {
				log.error(UtilwithAbasConnection.getMessage("pdmDocument.restservice.procad.error.FilenotFound",
						pdmDocument2.getUrlDocFile(), e.getMessage()));
				pdmDocument2
						.addError(UtilwithAbasConnection.getMessage("pdmDocument.restservice.procad.error.FilenotFound",
								pdmDocument2.getUrlDocFile(), e.getMessage()));
			} catch (InternalServerErrorException e) {
				log.error(UtilwithAbasConnection.getMessage("pdmDocument.restservice.procad.error.InternalServerError",
						pdmDocument2.getUrlDocFile(), e.getMessage()));

				pdmDocument2.addError(
						UtilwithAbasConnection.getMessage("pdmDocument.restservice.procad.error.InternalServerError",
								pdmDocument2.getUrlDocFile(), e.getMessage()));

			}

		}
	}

	protected List<PdmDocument> filterPdmDocs(List<PdmDocument> pdmDocumentList, String[] fileTypList) {

		return (List<PdmDocument>) pdmDocumentList.stream()
				.filter(pdmDocument -> pdmDocument.checkFileListTyp(fileTypList)).collect(Collectors.toList());

	}

	protected String getTargetPath() {
		String targetPath = "rmtmp/pdmgetDocuments/" + TIMESTAMP + "/";
		// Sicherstellen das der TargetPath existiert
		File targetPathFile = new File(targetPath);
		if (!targetPathFile.exists()) {
			targetPathFile.mkdirs();
		}
		return targetPath;
	}

}
