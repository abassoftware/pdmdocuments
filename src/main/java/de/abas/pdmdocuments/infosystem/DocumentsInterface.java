package de.abas.pdmdocuments.infosystem;

import java.util.List;

import de.abas.pdmdocuments.infosystem.data.PdmDocument;

public interface DocumentsInterface {

	public void setServer(String serveradresse);

	public void setPasword(String password);

	public void setUser(String user);

	public Boolean testConnection() throws PdmDocumentsException;

	public List<PdmDocument> getAllDocuments(String abasIdNo, String[] filetyplist) throws PdmDocumentsException;

}
