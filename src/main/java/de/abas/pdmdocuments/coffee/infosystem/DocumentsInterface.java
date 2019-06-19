package de.abas.pdmdocuments.coffee.infosystem;

import java.util.List;

import de.abas.pdmdocuments.coffee.infosystem.data.PdmDocument;

public interface DocumentsInterface {

	public void setServer(String serveradresse);

	public void setProduct(String pdmProductID);

	public void setDocumentTyp(String pdmDocumentTyp);

	public void setPasword(String password);

	public void setUser(String user);

	public Boolean testConnection() throws PdmDocumentsException;

	public String searchPdmProductID(String abasIdNo) throws PdmDocumentsException;

	public List<PdmDocument> getAllDocuments(String abasIdNo, String[] filetyplist) throws PdmDocumentsException;

}
