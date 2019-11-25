package de.abas.pdmdocuments.infosystem.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.log4j.Logger;

import de.abas.erp.db.schema.userenums.UserEnumPdmSystems;
import de.abas.pdmdocuments.infosystem.PdmDocumentsException;
import de.abas.pdmdocuments.infosystem.utils.UtilwithAbasConnection;

public class ConfigurationHandler {

	protected static final Logger log = Logger.getLogger(ConfigurationHandler.class);
	private static final String CONFIGFILE = "owpdm/pdmDocuments.config.properties";
	private static final String PDM_CONFIG_SERVER = "pdm.config.server";
	private static final String PDM_CONFIG_USER = "pdm.config.user";
	private static final String PDM_CONFIG_PASSWORD = "pdm.config.password";
	private static final String PDM_CONFIG_TENANT = "pdm.config.tenant";
	private static final String PDM_CONFIG_FIELD_FOR_PARTNUMBER = "pdm.config.fieldforpartnumber";
	private static final String PDM_CONFIG_FIELD_FOR_PARTPROFILEID = "pdm.config.fieldforpartProFileID";
	private static final String PDM_CONFIG_FIELD_FOR_ORGNAME = "pdm.config.fieldforOrgName";
	private static final String PDM_CONFIG_FIELD_FOR_DOCVERSIONBASEID = "pdm.config.fieldforDocVersionBaseID";
	private static final String PDM_CONFIG_FIELD_FOR_DOCTYPE = "pdm.config.fieldforDocType";
	private static final String PDM_CONFIG_PDMSYSTEM = "pdm.config.pdmsystem";
	private static final String PDM_CONFIG_SQL_SERVER = "pdm.config.sqlserver.server";
	private static final String PDM_CONFIG_SQL_PORT = "pdm.config.sqlserver.port";
	private static final String PDM_CONFIG_SQL_DATABASE = "pdm.config.sqlserver.database";
	private static final String PDM_CONFIG_SQL_USER = "pdm.config.sqlserver.user";
	private static final String PDM_CONFIG_SQL_PASSWORD = "pdm.config.sqlserver.password";
	private static final String PDM_CONFIG_SQL_DRIVER = "pdm.config.sqlsever.driver";
	private static final String PDM_CONFIG_FILETYPES_EMAIL = "pdm.config.filetypes.email";
	private static final String PDM_CONFIG_FILETYPES_PRINTER = "pdm.config.filetypes.printer";
	private static final String PDM_CONFIG_FILETYPES_SCREEN = "pdm.config.filetypes.screen";
	private static final String PDM_CONFIG_DOKART = "pdm.config.dokart";

	private ConfigurationHandler() {
		throw new IllegalStateException("Utility class should be private");
	}

	public static Configuration loadConfiguration() throws PdmDocumentsException {

		File propertiesFile = new File(CONFIGFILE);
		Configuration config = Configuration.getInstance();
		if (propertiesFile.exists()) {

			try (FileInputStream in = new FileInputStream(propertiesFile);) {

				Properties configProperties = new Properties();
				configProperties.load(in);

				// config contains all properties read from the file
				String restServer = configProperties.getProperty(PDM_CONFIG_SERVER);
				String restUser = configProperties.getProperty(PDM_CONFIG_USER);
				String restPassword = configProperties.getProperty(PDM_CONFIG_PASSWORD);
				String restTenant = configProperties.getProperty(PDM_CONFIG_TENANT);
				String pdmSystemString = configProperties.getProperty(PDM_CONFIG_PDMSYSTEM);

				UserEnumPdmSystems pdmSystem = UserEnumPdmSystems.valueOf(pdmSystemString);

				if (pdmSystem == null) {
					throw new PdmDocumentsException(
							UtilwithAbasConnection.getMessage("pdmDocument.error.pdmsystemnull", pdmSystemString));
				}

				String sqlServer = configProperties.getProperty(PDM_CONFIG_SQL_SERVER);
				String sqlPortString = configProperties.getProperty(PDM_CONFIG_SQL_PORT);
				String sqldatabase = configProperties.getProperty(PDM_CONFIG_SQL_DATABASE);
				String sqldriver = configProperties.getProperty(PDM_CONFIG_SQL_DRIVER);

				Integer sqlPort = null;
				if (sqlPortString != null && !sqlPortString.isEmpty()) {
					sqlPort = new Integer(sqlPortString);
				}
				String sqlUser = configProperties.getProperty(PDM_CONFIG_SQL_USER);
				String sqlPassword = configProperties.getProperty(PDM_CONFIG_SQL_PASSWORD);

				String fileTypesEmail = configProperties.getProperty(PDM_CONFIG_FILETYPES_EMAIL);
				String fileTypesPrinter = configProperties.getProperty(PDM_CONFIG_FILETYPES_PRINTER);
				String fileTypesScreen = configProperties.getProperty(PDM_CONFIG_FILETYPES_SCREEN);
				String fieldForPartNumber = configProperties.getProperty(PDM_CONFIG_FIELD_FOR_PARTNUMBER);
				String fieldforPartProFileID = configProperties.getProperty(PDM_CONFIG_FIELD_FOR_PARTPROFILEID);
				String fieldforOrgName = configProperties.getProperty(PDM_CONFIG_FIELD_FOR_ORGNAME);
				String fieldforDocVersionBaseID = configProperties.getProperty(PDM_CONFIG_FIELD_FOR_DOCVERSIONBASEID);
				String fieldforDocType = configProperties.getProperty(PDM_CONFIG_FIELD_FOR_DOCTYPE);

				String dokart = configProperties.getProperty(PDM_CONFIG_DOKART);

				config.initConfiguration(restServer, restUser, restPassword, restTenant, fieldForPartNumber,
						fieldforPartProFileID, fieldforOrgName, fieldforDocVersionBaseID, fieldforDocType, pdmSystem,
						sqlServer, sqlPort, sqldatabase, sqldriver, sqlUser, sqlPassword, fileTypesEmail,
						fileTypesPrinter, fileTypesScreen, dokart);

			} catch (IOException e) {
				throw new PdmDocumentsException(
						UtilwithAbasConnection.getMessage("pdmDocument.error.loadKonfiguration"), e);
			} catch (NumberFormatException e) {
				throw new PdmDocumentsException(UtilwithAbasConnection.getMessage("pdmDocument.error.sqlport"), e);
			}

		} else {
			throw new PdmDocumentsException(
					UtilwithAbasConnection.getMessage("pdmDocument.error.loadKonfiguration.noFile"));
		}

		return config;

	}

	public static void saveConfigurationtoFile(Configuration config) throws PdmDocumentsException {
		config.checkConfig();

		File propertiesFile = new File(CONFIGFILE);

		Properties configProperties = new Properties();

		configProperties.setProperty(PDM_CONFIG_SERVER, config.getRestServer());
		configProperties.setProperty(PDM_CONFIG_USER, config.getRestUser());
		configProperties.setProperty(PDM_CONFIG_PASSWORD, config.getRestPassword());
		configProperties.setProperty(PDM_CONFIG_TENANT, config.getRestTenant());

		configProperties.setProperty(PDM_CONFIG_PDMSYSTEM, config.getPdmSystem().name());

		configProperties.setProperty(PDM_CONFIG_SQL_SERVER, config.getSqlServer());
		configProperties.setProperty(PDM_CONFIG_SQL_PORT, config.getSqlPortString());
		configProperties.setProperty(PDM_CONFIG_SQL_DATABASE, config.getSqldatabase());
		configProperties.setProperty(PDM_CONFIG_SQL_USER, config.getSqlUser());
		configProperties.setProperty(PDM_CONFIG_SQL_PASSWORD, config.getSqlPassword());
		configProperties.setProperty(PDM_CONFIG_SQL_DRIVER, config.getSqlDriver());

		configProperties.setProperty(PDM_CONFIG_FILETYPES_EMAIL, config.getFileTypesEmail());
		configProperties.setProperty(PDM_CONFIG_FILETYPES_PRINTER, config.getFileTypesPrinter());
		configProperties.setProperty(PDM_CONFIG_FILETYPES_SCREEN, config.getFileTypesScreen());
		configProperties.setProperty(PDM_CONFIG_FIELD_FOR_PARTNUMBER, config.getPartFieldName());
		configProperties.setProperty(PDM_CONFIG_FIELD_FOR_PARTPROFILEID, config.getPartProFileIDFieldName());
		configProperties.setProperty(PDM_CONFIG_FIELD_FOR_ORGNAME, config.getOrgNameFieldName());
		configProperties.setProperty(PDM_CONFIG_FIELD_FOR_DOCVERSIONBASEID, config.getDocVersionBaseIDFieldName());
		configProperties.setProperty(PDM_CONFIG_FIELD_FOR_DOCTYPE, config.getDocTypeFieldName());

		configProperties.setProperty(PDM_CONFIG_DOKART, config.getDokart());

		FileOutputStream out;
		try {
			checkandCreateConfigFile(propertiesFile);
			out = new FileOutputStream(propertiesFile);
			configProperties.store(out, "---config PDMDocuments---");
			out.close();
		} catch (IOException e) {
			throw new PdmDocumentsException(UtilwithAbasConnection.getMessage("pdmDocument.error.saveConfiguration"));
		}
	}

	private static void checkandCreateConfigFile(File propertiesFile) throws IOException {
		if (!propertiesFile.exists() && !propertiesFile.getParentFile().exists()) {

			Files.createDirectory(Paths.get(propertiesFile.toPath().getParent().toUri()));
			Files.createFile(propertiesFile.toPath());

		}
	}

	protected static String getCONFIGFile() {
		return CONFIGFILE;

	}

}
