package de.abas.pdmdocuments.infosystem.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.abas.erp.db.schema.userenums.UserEnumPdmSystems;
import de.abas.pdmdocuments.infosystem.PdmDocumentsException;
import de.abas.pdmdocuments.infosystem.utils.UtilwithAbasConnection;

public class ConfigurationTest {

	private static final String HOST = "localhost";
	private static final String DOKART = "dokart";
	private static final String FILE_TYPES_SCREEN = "fileTypesScreen";
	private static final String FILE_TYPES_PRINTER = "fileTypesPrinter";
	private static final String FILE_TYPES_EMAIL = "fileTypesEmail";

	@Test
	public void testGetInstance() {
		Configuration config = Configuration.getInstance();
		Configuration config2 = Configuration.getInstance();

		Assertions.assertTrue(config.getClass().equals(Configuration.class));
		// Get Instance delivery always the same Object
		Assertions.assertEquals(config, config2);
	}

	@Test
	public void testInitConfiguration() {
		Configuration config = Configuration.getInstance();

		try {
			config.initConfiguration(HOST, "restUser", "restPassword", "restTenant", "partFieldName",
					"partProFileIDFieldName", "fieldforOrgName", "fieldforDocVersionBaseID", "fieldforDocType",
					UserEnumPdmSystems.KEYTECH, "sqlServer", 2222, "sqldatabase", "sqlDriver", "sqlUser", "sqlPassword",
					FILE_TYPES_EMAIL, FILE_TYPES_PRINTER, FILE_TYPES_SCREEN, DOKART);
			Assertions.assertEquals(HOST, config.getRestServer());
			Assertions.assertEquals("restUser", config.getRestUser());
			Assertions.assertEquals("restPassword", config.getRestPassword());
			Assertions.assertEquals("restTenant", config.getRestTenant());
			Assertions.assertEquals("partFieldName", config.getPartFieldName());
			Assertions.assertEquals("partProFileIDFieldName", config.getPartProFileIDFieldName());
			Assertions.assertEquals(UserEnumPdmSystems.KEYTECH, config.getPdmSystem());
			Assertions.assertEquals("sqlServer", config.getSqlServer());
			Assertions.assertEquals(new Integer("2222"), config.getSqlPort());
			Assertions.assertEquals("2222", config.getSqlPortString());
			Assertions.assertEquals("sqldatabase", config.getSqldatabase());
			Assertions.assertEquals("sqlDriver", config.getSqlDriver());
			Assertions.assertEquals("sqlUser", config.getSqlUser());
			Assertions.assertEquals("sqlPassword", config.getSqlPassword());
			Assertions.assertEquals(FILE_TYPES_EMAIL, config.getFileTypesEmail());
			Assertions.assertEquals(FILE_TYPES_PRINTER, config.getFileTypesPrinter());
			Assertions.assertEquals(FILE_TYPES_SCREEN, config.getFileTypesScreen());
			Assertions.assertEquals(DOKART, config.getDokart());

		} catch (PdmDocumentsException e) {
			Assertions.fail(e.getMessage());
		}

	}

	@Test
	public void testcheckFieldsForProfile() {

		Configuration config = Configuration.getInstance();

		try {
			config.initConfiguration("localhost1", "restUser1", "restPassword1", "restTenant1", "partFieldName",
					"partProFileIDFieldName", "fieldforOrgName", "fieldforDocVersionBaseID", "fieldforDocType",
					UserEnumPdmSystems.PROFILE, "sqlServer", 2222, "sqldatabase", "sqlDriver", "sqlUser", "sqlPassword",
					FILE_TYPES_EMAIL, FILE_TYPES_PRINTER, FILE_TYPES_SCREEN, DOKART);
			config.checkFieldsForProfile();
			config.setPartAbasNumberFieldName("");

			PdmDocumentsException exception = Assertions.assertThrows(PdmDocumentsException.class, () -> {
				config.checkFieldsForProfile();
			});

			assertEquals(UtilwithAbasConnection.getMessage("pdmDocument.error.profile.fieldnotset", "AbasNumber"),
					exception.getMessage());

			PdmDocumentsException exception0 = Assertions.assertThrows(PdmDocumentsException.class, () -> {
				config.checkFieldsForProfile();
			});

			assertEquals(UtilwithAbasConnection.getMessage("pdmDocument.error.profile.fieldnotset", "AbasNumber"),
					exception0.getMessage());

			config.setPartAbasNumberFieldName("partFieldName");

			config.setPartProFileIDFieldName("");
			PdmDocumentsException exception2 = Assertions.assertThrows(PdmDocumentsException.class, () -> {
				config.checkFieldsForProfile();
			});

			assertEquals(UtilwithAbasConnection.getMessage("pdmDocument.error.profile.fieldnotset", "PartID"),
					exception2.getMessage());
			config.setPartProFileIDFieldName("partProFileIDFieldName");

			config.setOrgNameFieldName("");
			PdmDocumentsException exception3 = Assertions.assertThrows(PdmDocumentsException.class, () -> {
				config.checkFieldsForProfile();
			});

			assertEquals(UtilwithAbasConnection.getMessage("pdmDocument.error.profile.fieldnotset", "orgName"),
					exception3.getMessage());
			config.setOrgNameFieldName("orgNameFieldName");

			config.setDocVersionBaseIDFieldName("");
			PdmDocumentsException exception4 = Assertions.assertThrows(PdmDocumentsException.class, () -> {
				config.checkFieldsForProfile();
			});

			assertEquals(UtilwithAbasConnection.getMessage("pdmDocument.error.profile.fieldnotset", "docVersionBaseID"),
					exception4.getMessage());
			config.setDocVersionBaseIDFieldName("docVersionBaseIDFieldName");

			config.setDocTypeFieldName("");
			PdmDocumentsException exception5 = Assertions.assertThrows(PdmDocumentsException.class, () -> {
				config.checkFieldsForProfile();
			});

			assertEquals(UtilwithAbasConnection.getMessage("pdmDocument.error.profile.fieldnotset", "docType"),
					exception5.getMessage());
			config.setDocVersionBaseIDFieldName("docTypeFieldName");

		} catch (PdmDocumentsException e) {

			Assertions.fail("Es darf keine Exeption geworfen werden!");
		}

	}

	@Test
	public void testSetRestServer() {
		Configuration config = Configuration.getInstance();

		try {
			config.initConfiguration("localhost1", "restUser1", "restPassword1", "restTenant1", "partFieldName",
					"partProFileIDFieldName", "fieldforOrgName", "fieldforDocVersionBaseID", "fieldforDocType",
					UserEnumPdmSystems.KEYTECH, "sqlServer", 2222, "sqldatabase", "sqlDriver", "sqlUser", "sqlPassword",
					FILE_TYPES_EMAIL, FILE_TYPES_PRINTER, FILE_TYPES_SCREEN, DOKART);

			Assertions.assertEquals("localhost1", config.getRestServer());
			Assertions.assertEquals("restUser1", config.getRestUser());
			Assertions.assertEquals("restPassword1", config.getRestPassword());
			Assertions.assertEquals("restTenant1", config.getRestTenant());

			config.setRestServer("localhost2", "restUser2", "restPassword2", "restTenant2");

			Assertions.assertEquals("localhost2", config.getRestServer());
			Assertions.assertEquals("restUser2", config.getRestUser());
			Assertions.assertEquals("restPassword2", config.getRestPassword());
			Assertions.assertEquals("restTenant2", config.getRestTenant());
			Assertions.assertThrows(PdmDocumentsException.class,
					() -> config.setRestServer("", "restUser2", "restPassword2", "restTenant2"));
		} catch (PdmDocumentsException e) {
			Assertions.fail(e.getMessage());
		}

	}

	@Test
	public void testSetSqlConnection() {
		Configuration config = Configuration.getInstance();
		try {
			config.initConfiguration(HOST, "restUser", "restPassword", "restTenant", "partFieldName",
					"partProFileIDFieldName", "fieldforOrgName", "fieldforDocVersionBaseID", "fieldforDocType",
					UserEnumPdmSystems.KEYTECH, "sqlServer", Integer.valueOf(2222), "sqldatabase", "sqlDriver",
					"sqlUser", "sqlPassword", FILE_TYPES_EMAIL, FILE_TYPES_PRINTER, FILE_TYPES_SCREEN, DOKART);

			Assertions.assertEquals("sqlServer", config.getSqlServer());
			Assertions.assertEquals("sqlUser", config.getSqlUser());
			Assertions.assertEquals("sqlPassword", config.getSqlPassword());
			Assertions.assertEquals("sqlDriver", config.getSqlDriver());
			Assertions.assertEquals("sqldatabase", config.getSqldatabase());
			Assertions.assertEquals(Integer.valueOf(2222), config.getSqlPort());

			config.setSqlConnection("sqlServer2", 3333, "sqldatabase2", "sqlUser2", "sqlPassword2", "sqlDriver2");

			Assertions.assertEquals("sqlServer2", config.getSqlServer());
			Assertions.assertEquals("sqlUser2", config.getSqlUser());
			Assertions.assertEquals("sqlPassword2", config.getSqlPassword());
			Assertions.assertEquals("sqlDriver2", config.getSqlDriver());
			Assertions.assertEquals("sqldatabase2", config.getSqldatabase());
			Assertions.assertEquals(Integer.valueOf(3333), config.getSqlPort());

		} catch (PdmDocumentsException e) {

			Assertions.fail(e.getMessage());
		}

	}

	@Test
	public void testSetFiletypes() {
		Configuration config = Configuration.getInstance();

		String fileTypesEmail = config.getFileTypesEmail();
		String fileTypesScreen = config.getFileTypesScreen();
		String fileTypesPrinter = config.getFileTypesPrinter();
		String fileTypesEmailNew = fileTypesEmail + "1";
		String fileTypesScreenNew = fileTypesScreen + "1";
		String fileTypesPrinterNew = fileTypesPrinter + "1";
		config.setFiletypes(fileTypesEmailNew, fileTypesPrinterNew, fileTypesScreenNew);
		Assertions.assertEquals(fileTypesEmailNew, config.getFileTypesEmail());
		Assertions.assertNotEquals(fileTypesEmail, config.getFileTypesEmail());
		Assertions.assertEquals(fileTypesScreenNew, config.getFileTypesScreen());
		Assertions.assertNotEquals(fileTypesScreen, config.getFileTypesScreen());
		Assertions.assertEquals(fileTypesPrinterNew, config.getFileTypesPrinter());
		Assertions.assertNotEquals(fileTypesPrinter, config.getFileTypesPrinter());
	}

	@Test
	public void testcheckRestServerInfo() {
		Configuration config = Configuration.getInstance();
		Assertions.assertThrows(PdmDocumentsException.class, () -> config.checkRestServerInfo(null),
				"If checkRestServer runs with null it should throw an PdmDocumentsException");
		Assertions.assertThrows(PdmDocumentsException.class, () -> config.checkRestServerInfo(""),
				"If checkRestServer runs with an empty String it should throw an PdmDocumentsException");

		try {
			Assertions.assertTrue(config.checkRestServerInfo("server"));
		} catch (PdmDocumentsException e) {
			Assertions.fail("It shouldn´t throw an Exception");

		}

	}

	@Test
	public void testGetSqlPort() {
		Configuration config = Configuration.getInstance();
		try {
			config.initConfiguration(HOST, "restUser", "restPassword", "restTenant", "partFieldName",
					"partProFileIDFieldName", "fieldforOrgName", "fieldforDocVersionBaseID", "fieldforDocType",
					UserEnumPdmSystems.KEYTECH, "sqlServer", null, "sqldatabase", "sqlDriver", "sqlUser", "sqlPassword",
					FILE_TYPES_EMAIL, FILE_TYPES_PRINTER, FILE_TYPES_SCREEN, DOKART);
			Assertions.assertEquals(null, config.getSqlPort());
		} catch (PdmDocumentsException e) {
			Assertions.fail(e.getMessage());

		}

	}

	@Test
	public void testGetSqlPortString() {
		Configuration config = Configuration.getInstance();
		try {
			config.initConfiguration(HOST, "restUser", "restPassword", "restTenant", "partFieldName",
					"partProFileIDFieldName", "fieldforOrgName", "fieldforDocVersionBaseID", "fieldforDocType",
					UserEnumPdmSystems.KEYTECH, "sqlServer", null, "sqldatabase", "sqlDriver", "sqlUser", "sqlPassword",
					FILE_TYPES_EMAIL, FILE_TYPES_PRINTER, FILE_TYPES_SCREEN, DOKART);
			Assertions.assertEquals("", config.getSqlPortString());
		} catch (PdmDocumentsException e) {
			Assertions.fail(e.getMessage());

		}

	}

//	
	@Test
	public void testSetPdmSystem() {
		Configuration config = Configuration.getInstance();
		config.setPdmSystem(UserEnumPdmSystems.COFFEE);
		Assertions.assertEquals(UserEnumPdmSystems.COFFEE, config.getPdmSystem());
		config.setPdmSystem(null);
		Assertions.assertEquals(null, config.getPdmSystem());

	}

	@Test
	public void testSetPartFieldName() {
		Configuration config = Configuration.getInstance();

		config.setPartAbasNumberFieldName("partfield");

		Assertions.assertEquals("partfield", config.getPartFieldName());

	}

	@Test
	public void testSetPartProFileIDFieldName() {
		Configuration config = Configuration.getInstance();
		config.setPartProFileIDFieldName("partProFileIDFieldName");

		Assertions.assertEquals("partProFileIDFieldName", config.getPartProFileIDFieldName());
	}

	@Test
	public void testSetSqlDriver() {
		Configuration config = Configuration.getInstance();
		config.setSqlDriver("sqlDriver");
		Assertions.assertEquals("sqlDriver", config.getSqlDriver());
	}

	@Test
	public void testGetDokart() {
		Configuration config = Configuration.getInstance();
		config.setDokart("testdokart");
		Assertions.assertEquals("testdokart", config.getDokart());
	}

//
}
