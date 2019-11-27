package de.abas.pdmdocuments.infosystem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.abas.erp.db.infosystem.custom.owpdm.PdmDocuments;
import de.abas.erp.db.infosystem.custom.owpdm.PdmDocuments.Row;
import de.abas.pdmdocuments.infosystem.utils.Util;
import de.abas.pdmdocuments.infosystem.utils.UtilwithAbasConnection;

public class AttachmentFileManager {
	private static final String ERROR_CREATE_TEMPFILE = "error.create.Tempfile";
	protected static final Logger log = Logger.getLogger(AttachmentFileManager.class);

	private AttachmentFileManager() {
		throw new UnsupportedOperationException();
	}

	protected static void deletePathAtAttachmentlist(String ypfad, PdmDocuments head) throws PdmDocumentsException {
		try {
			File tempFile = getTempFileYanhangliste(head);

			deleteFilenametoFile(tempFile, ypfad);

		} catch (IOException e) {
			log.error(e);
			throw new PdmDocumentsException(UtilwithAbasConnection.getMessage(ERROR_CREATE_TEMPFILE, e.getMessage()));

		}
	}

	protected static void addPathAtAttachmentlist(String ypfad, PdmDocuments head) throws PdmDocumentsException {
		try {
			File tempFile = getTempFileYanhangliste(head);

			addFilenametoFile(tempFile, ypfad);

		} catch (IOException e) {
			log.error(e);
			throw new PdmDocumentsException(UtilwithAbasConnection.getMessage(ERROR_CREATE_TEMPFILE, e.getMessage()));
		}

	}

	protected static void deleteFilenameAtYuebdatei(String ydateiname, PdmDocuments head) throws PdmDocumentsException {
		try {
			File tempFile = getTempFileYuebdatei(head);

			deleteFilenametoFile(tempFile, ydateiname);

		} catch (IOException e) {
			log.error(e);
			throw new PdmDocumentsException(UtilwithAbasConnection.getMessage(ERROR_CREATE_TEMPFILE, e.getMessage()));

		}

	}

	private static void deleteFilenametoFile(File tempFile, String ydateiname) throws IOException {

		List<String> fileListArray = Util.readStringListFromFile(tempFile);

		if (fileListArray.contains(ydateiname)) {
			fileListArray.remove(ydateiname);
		}

		Util.writeStringtoFile(tempFile, fileListArray);

	}

	protected static void addFilenameAtYuebdatei(String ydateiname, PdmDocuments head) throws PdmDocumentsException {
		try {
			File tempFile = getTempFileYuebdatei(head);

			addFilenametoFile(tempFile, ydateiname);

		} catch (IOException e) {
			log.error(e);
			throw new PdmDocumentsException(UtilwithAbasConnection.getMessage(ERROR_CREATE_TEMPFILE, e.getMessage()));

		}

	}

	private static void addFilenametoFile(File tempFile, String ydateiname) throws IOException {

		List<String> fileListArray = Util.readStringListFromFile(tempFile);

		if (!fileListArray.contains(ydateiname)) {
			fileListArray.add(ydateiname);
		}

		Util.writeStringtoFile(tempFile, fileListArray);
	}

	protected static File getTempFileYanhangliste(PdmDocuments head) throws IOException {

		String emailAttachmentFile = head.getYanhangliste();

		return getTempFile(emailAttachmentFile, "pdmDoc", ".TMP", "rmtmp");

	}

	protected static File getTempFileYuebdatei(PdmDocuments head) throws IOException {
		File tempFile = null;
		String yuebdatei = head.getYuebdatei();

		tempFile = getTempFile(yuebdatei, "pdmDocUeb", ".TMP", "rmtmp");

		head.setYuebdatei(tempFile.getAbsolutePath());

		return tempFile;
	}

	private static File getTempFile(String fileName, String praefix, String suffix, String tempVerz)
			throws IOException {

		File tmpverz = new File(tempVerz);
		File tempFile;

		if (fileName.isEmpty()) {
			tempFile = File.createTempFile(praefix, suffix, tmpverz);

			return tempFile;
		} else {
			tempFile = new File(fileName);
			if (!tempFile.exists()) {
				tempFile.createNewFile();
			}
			return tempFile;
		}

	}

	protected static List<String> getyuebDateiArray(PdmDocuments head) throws IOException {
		List<String> fileListArray = new ArrayList<>();
		if (!head.getYuebdatei().isEmpty()) {
			File tempFile = new File(head.getYuebdatei());

			fileListArray = Util.readStringListFromFile(tempFile);
		}
		return fileListArray;
	}

	public static void writeAttachmentfile(Iterable<Row> rows, PdmDocuments head) throws IOException {

		try (FileWriter fileWriter = new FileWriter(getTempFileYanhangliste(head))) {

			for (Row row : rows) {
				if (!row.getYpfad().isEmpty()) {
					// Kopieren für FOPMULTI in das Output-Verzeichnis
					if (head.getYistemail()) {
						// In die Dateianhangliste eintragen
						fileWriter.append(row.getYpfad() + System.getProperty("line.separator"));
					}
				}
			}

		}

	}

}
