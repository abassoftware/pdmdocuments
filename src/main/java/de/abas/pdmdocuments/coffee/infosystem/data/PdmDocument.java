package de.abas.pdmdocuments.coffee.infosystem.data;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import de.abas.pdmdocuments.coffee.infosystem.PdmDocumentsException;
import de.abas.pdmdocuments.coffee.infosystem.utils.DocumentsUtil;
import de.abas.pdmdocuments.coffee.infosystem.utils.Util;

public class PdmDocument {

	private static final String PDM_DOCUMENT_META_DATA_LIST_DOUBLE_VALUE = "pdmDocument.metaDataList.doubleValue";

	protected static final Logger log = Logger.getLogger(PdmDocument.class);

	private File file;
	private String filename;
	private String filetyp;
	private String documenttyp;
	private String pageformat;
	private String urlDocFile;
	private String error = "";

	HashMap<String, DocMetaData> metaDataList;

	public PdmDocument(File file, String documenttyp) throws PdmDocumentsException {
		super();
		this.file = file;
		this.filename = this.file.getName();
		this.filetyp = DocumentsUtil.getFileExtension(this.file);
		this.metaDataList = new HashMap<>();
		this.documenttyp = documenttyp;
		try {
			this.pageformat = DocumentsUtil.getPageFormat(this.file);
		} catch (IOException e) {
			throw new PdmDocumentsException(Util.getMessage("pdmDocument.formatcheck.error.io"));
		}
	}

	public PdmDocument(String filename, String documenttyp, String urlDocFile) {
		super();
		this.urlDocFile = urlDocFile;
		this.filename = filename;
		this.filetyp = DocumentsUtil.getFileExtensionFromName(filename);
		this.documenttyp = documenttyp;
		this.metaDataList = new HashMap<>();

	}

	public void addFile(File file) {

		this.file = file;
		this.filename = this.file.getName();
		this.filetyp = DocumentsUtil.getFileExtension(this.file);
		try {
			this.pageformat = DocumentsUtil.getPageFormat(this.file);
		} catch (IOException e) {
			this.error = this.error + " " + Util.getMessage("pdmDocument.formatcheck.error.io");
		}
	}

	public String getUrlDocFile() {
		return urlDocFile;
	}

	public String getDocumenttyp() {
		return documenttyp;
	}

	public void addDocMetaData(String valueName, String value) throws PdmDocumentsException {

		DocMetaData docMetaData = new DocMetaData(valueName, value);

		if (!metaDataList.containsKey(valueName)) {
			this.metaDataList.put(valueName, docMetaData);
		} else {
			throw new PdmDocumentsException(Util.getMessage(PDM_DOCUMENT_META_DATA_LIST_DOUBLE_VALUE, valueName));
		}

	}

	public void addDocMetaData(String valueName, Integer value) throws PdmDocumentsException {

		DocMetaData docMetaData = new DocMetaData(valueName, value);

		if (!metaDataList.containsKey(valueName)) {
			this.metaDataList.put(valueName, docMetaData);
		} else {
			throw new PdmDocumentsException(Util.getMessage(PDM_DOCUMENT_META_DATA_LIST_DOUBLE_VALUE, valueName));
		}

	}

	public void addDocMetaData(String valueName, Date value) throws PdmDocumentsException {

		DocMetaData docMetaData = new DocMetaData(valueName, value);

		if (!metaDataList.containsKey(valueName)) {
			this.metaDataList.put(valueName, docMetaData);
		} else {
			throw new PdmDocumentsException(Util.getMessage(PDM_DOCUMENT_META_DATA_LIST_DOUBLE_VALUE, valueName));
		}

	}

	public void addDocMetaData(String valueName, BigDecimal value) throws PdmDocumentsException {

		DocMetaData docMetaData = new DocMetaData(valueName, value);

		if (!metaDataList.containsKey(valueName)) {
			this.metaDataList.put(valueName, docMetaData);
		} else {
			throw new PdmDocumentsException(Util.getMessage(PDM_DOCUMENT_META_DATA_LIST_DOUBLE_VALUE, valueName));
		}

	}

	public DocMetaData getDocMetaDataByName(String valueName) {

		if (this.metaDataList.containsKey(valueName)) {
			return this.metaDataList.get(valueName);
		} else {
			return null;
		}
	}

	public void addDocMetaData(String valueName, Object value) throws PdmDocumentsException {
		DocMetaData docMetaData = new DocMetaData(valueName, value);

		if (!metaDataList.containsKey(valueName)) {
			this.metaDataList.put(valueName, docMetaData);
		} else {
			throw new PdmDocumentsException(Util.getMessage(PDM_DOCUMENT_META_DATA_LIST_DOUBLE_VALUE, valueName));
		}
	}

	public Boolean hasFile() {
		return (this.file == null);
	}

	public File getFile() {
		return file;
	}

	public String getPageformat() {
		return pageformat;
	}

	public String getFilename() {
		return filename;
	}

	public String getFiletyp() {
		return filetyp;
	}

	public Collection<DocMetaData> getMetaDataList() {
		return this.metaDataList.values();
	}

	public String getError() {
		return error;
	}

	public void addError(String errortxt) {
		if (this.error.isEmpty()) {
			this.error = errortxt;
		} else {
			this.error = this.error + ";" + errortxt;
		}
	}

	public boolean hasError() {

		return this.error.isEmpty();
	}

	public Boolean checkFileListTyp(String[] fileListTyp) {

		Boolean allempty = true;

		if (fileListTyp != null && fileListTyp.length > 0) {

			for (String typ : fileListTyp) {
				if (!typ.isEmpty()) {
					if (typ.trim().equalsIgnoreCase(this.filetyp)) {
						log.trace(Util.getMessage("pdmDocument.checkdocument.includePdmDoc", this.getFilename(),
								Arrays.toString(fileListTyp)));
						return true;
					}
					allempty = false;
				}
			}

		}
		if (allempty) {
			log.trace(Util.getMessage("pdmDocument.checkdocument.includePdmDoc.emptyTypliste", this.getFilename()));
			return true;
		} else {
			log.trace(Util.getMessage("pdmDocument.checkdocument.excludePdmDoc", this.getFilename(),
					Arrays.toString(fileListTyp)));
			return false;
		}

	}

	public Boolean checkFileNameList(List<String> fileNameList) {

		String pdmFileName = this.filename;
		Boolean allempty = true;

		if (fileNameList != null && !fileNameList.isEmpty()) {

			for (String filenameFromList : fileNameList) {
				if (!filenameFromList.isEmpty()) {
					if (filenameFromList.trim().equalsIgnoreCase(pdmFileName)) {
						log.trace(Util.getMessage("pdmDocument.checkdocument.includeFilenameList", this.getFilename()));
						return true;
					}
					allempty = false;
				}
			}
		}
		if (allempty) {
			log.trace(Util.getMessage("pdmDocument.checkdocument.includeFilenameList.emptyList", this.getFilename()));
			return true;
		} else {
			log.trace(Util.getMessage("pdmDocument.checkdocument.excludeFilenameList", this.getFilename()));
			return false;
		}

	}

	public Boolean checkDocTypList(List<String> doctypList) {

		String pdmDocumenttyp = this.documenttyp;
		Boolean allempty = true;

		if (doctypList != null && !doctypList.isEmpty()) {

			for (String doctyp : doctypList) {
				if (!doctyp.isEmpty()) {
					if (doctyp.trim().equalsIgnoreCase(pdmDocumenttyp)) {
						log.trace(Util.getMessage("pdmDocument.checkdocument.includeDoctypList", this.getFilename()));
						return true;
					}
					allempty = false;
				}
			}
		}
		if (allempty) {
			log.trace(Util.getMessage("pdmDocument.checkdocument.includeDoctypList.emptyList", this.getFilename()));
			return true;
		} else {
			log.trace(Util.getMessage("pdmDocument.checkdocument.excludeDoctypList", this.getFilename()));
			return false;
		}

	}

}
