package de.abas.pdmdocuments.infosystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import de.abas.erp.db.DbContext;
import de.abas.erp.db.infosystem.custom.owpdm.PdmDocuments.Row;
import de.abas.pdmdocuments.infosystem.utils.Util;
import de.abas.pdmdocuments.infosystem.utils.UtilwithAbasConnection;

public class DocumentsFileManager {

	private DocumentsFileManager() {
		throw new UnsupportedOperationException();
	}

	protected static void renameFiles(Iterable<Row> rows, DbContext ctx) throws PdmDocumentsException {
		String number = "";
		String name = "";
		for (Row row : rows) {

			if (row.getYtartikel() != null) {
				number = row.getYtartikel().getIdno();
				name = row.getYtartikel().getDescr();
			}
			if (!row.getYpfad().isEmpty()) {
				String docId = extractDocId(row);

				move2newFiles(number, name, row, docId);

			}
		}
	}

	private static void move2newFiles(String number, String name, Row row, String docId) throws PdmDocumentsException {
		String oldPath = row.getYpfad();
		String newPath = oldPath.substring(0, oldPath.lastIndexOf("/"));

		String newFilename = getNewFileName(number, name, row, docId);

		String newcompletePath = newPath + "/" + newFilename;
		File orgFile = new File(oldPath);
		File newFile = new File(newcompletePath);
		try {
			if (orgFile.exists()) {
				if (newFile.exists()) {
					Files.move(orgFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				} else {
					Files.move(orgFile.toPath(), newFile.toPath(), StandardCopyOption.ATOMIC_MOVE);
				}
				actFileFields(row, newFilename, newcompletePath);
			} else {
				if (newFile.exists()) {
					actFileFields(row, newFilename, newcompletePath);
				}
			}

		} catch (IOException e) {
			throw new PdmDocumentsException(UtilwithAbasConnection.getMessage("error.renameFile.move"), e);
		}
	}

	private static String getNewFileName(String number, String name, Row row, String docId) {
		String fileExtension = row.getYdatend();
		String newFilename = number + "_" + name + "_" + docId;
		newFilename = Util.replaceUmlaute(newFilename.replaceAll(" ", "_"));
		newFilename = Util.replaceSonderzeichen(newFilename);
		newFilename = newFilename + "." + fileExtension;
		return newFilename;
	}

	private static String extractDocId(Row row) {
		String docId = "";
		if (row.getYmeta1key().equals("FileID")) {
			docId = row.getYmeta1value();
		} else if (row.getYdateiname().indexOf('_') != -1) {
			docId = row.getYdateiname().substring(0, row.getYdateiname().indexOf('_'));
		}
		return docId;
	}

	private static void actFileFields(Row row, String newFilename, String newcompletePath) {
		row.setYpfad(newcompletePath);
		row.setYdateiname(newFilename);
	}

}
