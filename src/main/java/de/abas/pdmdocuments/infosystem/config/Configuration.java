package de.abas.pdmdocuments.infosystem.config;

import de.abas.erp.db.schema.userenums.UserEnumPdmSystems;
import de.abas.pdmdocuments.infosystem.PdmDocumentsException;
import de.abas.pdmdocuments.infosystem.utils.UtilwithAbasConnection;

public class Configuration {

	private static Configuration instance;

	// Zugangsdaten für RestService
	private String restServer;
	private String restUser;
	private String restPassword;
	private String restTenant;

	// Felder für die Suche in ProFile
	private String partAbasNumberFieldName;

	private String partProFileIDFieldName;
	private String orgNameFieldName;

	private String docVersionBaseIDFieldName;

	private String docTypeFieldName;

	// Name des PDMSystem
	private UserEnumPdmSystems pdmSystem;

	// Zugangsdaten SQLServer
	private String sqlServer;
	private Integer sqlPort;
	private String sqldatabase;
	private String sqlUser;
	private String sqlPassword;
	private String sqlDriver;

	// Auswahl der DateiTypen
	private String fileTypesEmail;
	private String fileTypesPrinter;
	private String fileTypesScreen;

	// Auswahl Dokumententypen
	private String dokart;

	private Configuration() {
		this.restServer = null;
		this.restUser = null;
		this.restPassword = null;
		this.restTenant = null;
		this.pdmSystem = null;
		this.sqlServer = null;
		this.sqlPort = null;
		this.sqldatabase = null;
		this.sqlUser = null;
		this.sqlPassword = null;
		this.fileTypesEmail = null;
		this.fileTypesPrinter = null;
		this.fileTypesScreen = null;
		this.sqlDriver = null;
		this.dokart = null;
		this.partAbasNumberFieldName = "";
		this.partProFileIDFieldName = "";
		this.orgNameFieldName = "";
		this.docVersionBaseIDFieldName = "";
		this.docTypeFieldName = "";
	}

	public static synchronized Configuration getInstance() {
		if (Configuration.instance == null) {
			Configuration.instance = new Configuration();
		}
		return Configuration.instance;
	}

	public void initConfiguration(String restServer, String restUser, String restPassword, String restTenant,
			String partFieldName, String partProFileIDFieldName, String orgNameFieldName,
			String docVersionBaseIDFieldName, String docTypeFieldName, UserEnumPdmSystems pdmSystem, String sqlServer,
			Integer sqlPort, String sqldatabase, String sqlDriver, String sqlUser, String sqlPassword,
			String fileTypesEmail, String fileTypesPrinter, String fileTypesScreen, String dokart)
			throws PdmDocumentsException {

		checkRestServerInfo(restServer);
		this.restServer = restServer;
		this.restUser = restUser;
		this.restPassword = restPassword;
		this.restTenant = restTenant;
		this.partAbasNumberFieldName = partFieldName;
		this.partProFileIDFieldName = partProFileIDFieldName;
		this.orgNameFieldName = orgNameFieldName;
		this.docVersionBaseIDFieldName = docVersionBaseIDFieldName;
		this.docTypeFieldName = docTypeFieldName;

		this.pdmSystem = pdmSystem;
		this.sqlServer = sqlServer;
		this.sqlPort = sqlPort;
		this.sqldatabase = sqldatabase;
		this.sqlUser = sqlUser;
		this.sqlPassword = sqlPassword;
		this.sqlDriver = sqlDriver;
		this.fileTypesEmail = fileTypesEmail;
		this.fileTypesPrinter = fileTypesPrinter;
		this.fileTypesScreen = fileTypesScreen;
		this.dokart = dokart;
		checkPdmSystem();

	}

	private void checkPdmSystem() throws PdmDocumentsException {
		if (this.pdmSystem == null) {
			throw new PdmDocumentsException(UtilwithAbasConnection.getMessage("pdmDocument.error.pdmsystemnull", ""));
		}

	}

	public void checkConfig() throws PdmDocumentsException {
		checkPdmSystem();
		areAllFieldsforFieldnameSet();

	}

	private void areAllFieldsforFieldnameSet() throws PdmDocumentsException {
		if (this.pdmSystem.equals(UserEnumPdmSystems.PROFILE)) {
			checkFieldsForProfile();
		}

	}

	private void checkFieldsForProfile() throws PdmDocumentsException {
		if (this.partAbasNumberFieldName == null || this.partAbasNumberFieldName.isEmpty()) {
			throw new PdmDocumentsException(
					UtilwithAbasConnection.getMessage("pdmDocument.error.profile.fieldnotset", "AbasNumber"));
		}

		if (this.partProFileIDFieldName == null || this.partProFileIDFieldName.isEmpty()) {
			throw new PdmDocumentsException(
					UtilwithAbasConnection.getMessage("pdmDocument.error.profile.fieldnotset", "PartID"));
		}
		if (this.orgNameFieldName == null || this.orgNameFieldName.isEmpty()) {
			throw new PdmDocumentsException(
					UtilwithAbasConnection.getMessage("pdmDocument.error.profile.fieldnotset", "orgName"));
		}
		if (this.docVersionBaseIDFieldName == null || this.docVersionBaseIDFieldName.isEmpty()) {
			throw new PdmDocumentsException(
					UtilwithAbasConnection.getMessage("pdmDocument.error.profile.fieldnotset", "docVersionBaseID"));
		}
		if (this.docTypeFieldName == null || this.docTypeFieldName.isEmpty()) {
			throw new PdmDocumentsException(
					UtilwithAbasConnection.getMessage("pdmDocument.error.profile.fieldnotset", "docType"));
		}
	}

	public void setRestServer(String restServer, String restUser, String restPassword, String restTenant)
			throws PdmDocumentsException {
		if (checkRestServerInfo(restServer)) {
			this.restServer = restServer;
			this.restUser = restUser;
			this.restPassword = restPassword;
			this.restTenant = restTenant;
		}

	}

	public void setSqlConnection(String sqlServer, Integer sqlPort, String sqldatabase, String sqlUser,
			String sqlPassword, String sqlDriver) {

		this.sqlServer = sqlServer;
		this.sqlPort = sqlPort;
		this.sqldatabase = sqldatabase;
		this.sqlUser = sqlUser;
		this.sqlPassword = sqlPassword;
		this.sqlDriver = sqlDriver;

	}

	public void setFiletypes(String fileTypesEmail, String fileTypesPrinter, String fileTypesScreen) {
		this.fileTypesEmail = fileTypesEmail;
		this.fileTypesPrinter = fileTypesPrinter;
		this.fileTypesScreen = fileTypesScreen;
	}

	protected Boolean checkRestServerInfo(String restServer) throws PdmDocumentsException {
		if (restServer != null) {
			if (restServer.isEmpty()) {
				throw new PdmDocumentsException(
						UtilwithAbasConnection.getMessage("pdmDocument.error.restServerNotSet"));
			}
		} else {
			throw new PdmDocumentsException(UtilwithAbasConnection.getMessage("pdmDocument.error.restServerNotSet"));
		}

		return true;
	}

	public String getRestServer() {
		return restServer;
	}

	public String getRestUser() {
		return restUser;
	}

	public String getRestPassword() {
		return restPassword;
	}

	public String getRestTenant() {
		return restTenant;
	}

	public UserEnumPdmSystems getPdmSystem() {
		return pdmSystem;
	}

	public String getSqlServer() {
		return sqlServer;
	}

	public Integer getSqlPort() {
		return sqlPort;
	}

	public String getSqlPortString() {

		if (sqlPort == null) {
			return "";
		}
		return sqlPort.toString();
	}

	public String getSqlDriver() {
		return sqlDriver;
	}

	public String getSqldatabase() {
		return sqldatabase;
	}

	public String getSqlUser() {
		return sqlUser;
	}

	public String getSqlPassword() {
		return sqlPassword;
	}

	public String getFileTypesEmail() {
		return fileTypesEmail;
	}

	public String getFileTypesPrinter() {
		return fileTypesPrinter;
	}

	public String getFileTypesScreen() {
		return fileTypesScreen;
	}

	public void setPdmSystem(UserEnumPdmSystems ypdmsystem) {

		this.pdmSystem = ypdmsystem;

	}

	public String getPartFieldName() {
		return partAbasNumberFieldName;
	}

	public void setPartAbasNumberFieldName(String partFieldName) {
		this.partAbasNumberFieldName = partFieldName;
	}

	public String getPartProFileIDFieldName() {
		return partProFileIDFieldName;
	}

	public void setPartProFileIDFieldName(String partProFileIDFieldName) {
		this.partProFileIDFieldName = partProFileIDFieldName;
	}

	public void setSqlDriver(String sqlDriver) {
		this.sqlDriver = sqlDriver;
	}

	public String getDokart() {
		return dokart;
	}

	public void setDokart(String dokart) {
		this.dokart = dokart;

	}

	public String getOrgNameFieldName() {

		return this.orgNameFieldName;
	}

	public String getDocVersionBaseIDFieldName() {

		return this.docVersionBaseIDFieldName;
	}

	public String getDocTypeFieldName() {

		return this.docTypeFieldName;
	}

	public void setDocVersionBaseIDFieldName(String docVersionBaseIDFieldName) {
		this.docVersionBaseIDFieldName = docVersionBaseIDFieldName;
	}

	public void setDocTypeFieldName(String docTypeFieldName) {
		this.docTypeFieldName = docTypeFieldName;
	}

	public void setOrgNameFieldName(String orgNameFieldName) {
		this.orgNameFieldName = orgNameFieldName;
	}
}
