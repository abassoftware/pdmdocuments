package de.abas.pdmdocuments.infosystem.config;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.abas.erp.db.schema.userenums.UserEnumPdmSystems;
import de.abas.pdmdocuments.infosystem.PdmDocumentsException;
import de.abas.pdmdocuments.infosystem.utils.UtilwithAbasConnection;

public class ConfigurationHandlerTest {
	Configuration config = Configuration.getInstance();
	File configFile = new File(ConfigurationHandler.getCONFIGFile());

	@BeforeEach
	public void setUp() throws Exception {

		config.initConfiguration("localhost", "restUser", "restPassword", "restTenant", "partFieldName",
				"partProFileIDFieldName", "fieldforOrgName", "fieldforDocVersionBaseID", "fieldforDocType",
				UserEnumPdmSystems.KEYTECH, "sqlServer", 2222, "sqldatabase", "sqlDriver", "sqlUser", "sqlPassword",
				"fileTypesEmail", "fileTypesPrinter", "fileTypesScreen", "dokart");
		ConfigurationHandler.saveConfigurationtoFile(config);

	}

	@AfterEach
	public void tearDown() {
		deleteConfigFileandDirectory();
	}

	@Test
	public void testLoadConfiguration() {
		try {
			Configuration testconfig = ConfigurationHandler.loadConfiguration();
			Assertions.assertEquals(UserEnumPdmSystems.KEYTECH, testconfig.getPdmSystem());
		} catch (PdmDocumentsException e) {
			Assertions.fail(e.getMessage());

		}

		try {
			Configuration testconfig2 = ConfigurationHandler.loadConfiguration();
			testconfig2.setPdmSystem(null);
			ConfigurationHandler.saveConfigurationtoFile(testconfig2);
			Configuration testconfig3 = ConfigurationHandler.loadConfiguration();
			Assertions.fail("It should throw a PdmDocumentsException");
		} catch (PdmDocumentsException e) {
			Assertions.assertEquals(UtilwithAbasConnection.getMessage("pdmDocument.error.pdmsystemnull", ""),
					e.getMessage());

		}

	}

	@Test
	public void testConfigurationsHandler() {

		Assertions.assertThrows(IllegalAccessException.class, () -> ConfigurationHandler.class.newInstance(),
				"Utility class should be private");

	}

	@Test
	public void testSaveConfigurationtoFile() {
		configFile.delete();
		try {
			config.setPdmSystem(UserEnumPdmSystems.PROFILE);
			ConfigurationHandler.saveConfigurationtoFile(config);
			Assertions.assertTrue(configFile.exists());
		} catch (PdmDocumentsException e) {
			Assertions.fail(e.getMessage());
		}

	}

	private void deleteConfigFileandDirectory() {

		if (configFile.exists()) {
			configFile.delete();
		}

		configFile.getParentFile().delete();

	}

}
