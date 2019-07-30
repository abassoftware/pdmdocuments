package de.abas.pdmdocuments.infosystem.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.abas.erp.db.schema.userenums.UserEnumPdmSystems;
import de.abas.pdmdocuments.infosystem.PdmDocumentsException;
import de.abas.pdmdocuments.infosystem.config.Configuration;
import de.abas.pdmdocuments.infosystem.config.ConfigurationHandler;

public class ConfigurationHandlerTest {
	Configuration config = Configuration.getInstance();
	File configFile = new File(ConfigurationHandler.getCONFIGFile());

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {

		config.initConfiguration("localhost", "restUser", "restPassword", "restTenant", "partFieldName",
				"partProFileIDFieldName", UserEnumPdmSystems.KEYTECH, "sqlServer", 2222, "sqldatabase", "sqlDriver",
				"sqlUser", "sqlPassword", "fileTypesEmail", "fileTypesPrinter", "fileTypesScreen", "dokart");
		ConfigurationHandler.saveConfigurationtoFile(config);

	}

	@After
	public void tearDown() {
		deleteConfigFileandDirectory();
	}

	@Test
	public void testLoadConfiguration() {
		try {
			Configuration testconfig = ConfigurationHandler.loadConfiguration();
			assertEquals(UserEnumPdmSystems.KEYTECH, testconfig.getPdmSystem());
		} catch (PdmDocumentsException e) {
			fail(e.getMessage());

		}

	}

	@Test
	public void testSaveConfigurationtoFile() {
		configFile.delete();
		try {
			config.setPdmSystem(UserEnumPdmSystems.PROFILE);
			ConfigurationHandler.saveConfigurationtoFile(config);
			assertTrue(configFile.exists());
		} catch (PdmDocumentsException e) {
			fail(e.getMessage());
		}

	}

	private void deleteConfigFileandDirectory() {

		if (configFile.exists()) {
			configFile.delete();
		}

		configFile.getParentFile().delete();

	}

}
