package de.abas.pdmdocuments.infosystem;

import org.apache.commons.lang3.exception.ExceptionUtils;

public class PdmDocumentsException extends Exception {

	private static final long serialVersionUID = 66964017367429334L;

	public PdmDocumentsException(String message) {
		super(message);

	}

	public PdmDocumentsException(String message, Exception e) {
		super(message + "\n" + e.getMessage() + "\n" + ExceptionUtils.getStackTrace(e));

	}

}
