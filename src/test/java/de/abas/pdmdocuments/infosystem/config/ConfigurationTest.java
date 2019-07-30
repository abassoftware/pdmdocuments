package de.abas.pdmdocuments.infosystem.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.abas.erp.db.schema.userenums.UserEnumPdmSystems;
import de.abas.pdmdocuments.infosystem.PdmDocumentsException;
import de.abas.pdmdocuments.infosystem.config.Configuration;

public class ConfigurationTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetInstance() {
		Configuration config = Configuration.getInstance();
		Configuration config2 = Configuration.getInstance();

		assertTrue(config.getClass().equals(Configuration.class));
		// Get Instance delivery always the same Object
		assertEquals(config, config2);
	}

	@Test
	public void testInitConfiguration() {
		Configuration config = Configuration.getInstance();

//		assertNull(config.getPdmSystem());
		try {
			config.initConfiguration("localhost", "restUser", "restPassword", "restTenant", "partFieldName",
					"partProFileIDFieldName", UserEnumPdmSystems.KEYTECH, "sqlServer", 2222, "sqldatabase", "sqlDriver",
					"sqlUser", "sqlPassword", "fileTypesEmail", "fileTypesPrinter", "fileTypesScreen", "dokart");
			assertEquals("localhost", config.getRestServer());
			assertEquals("restUser", config.getRestUser());
			assertEquals("restPassword", config.getRestPassword());
			assertEquals("restTenant", config.getRestTenant());
			assertEquals("partFieldName", config.getPartFieldName());
			assertEquals("partProFileIDFieldName", config.getPartProFileIDFieldName());
			assertEquals(UserEnumPdmSystems.KEYTECH, config.getPdmSystem());
			assertEquals("sqlServer", config.getSqlServer());
			assertEquals(new Integer("2222"), config.getSqlPort());
			assertEquals("2222", config.getSqlPortString());
			assertEquals("sqldatabase", config.getSqldatabase());
			assertEquals("sqlDriver", config.getSqlDriver());
			assertEquals("sqlUser", config.getSqlUser());
			assertEquals("sqlPassword", config.getSqlPassword());
			assertEquals("fileTypesEmail", config.getFileTypesEmail());
			assertEquals("fileTypesPrinter", config.getFileTypesPrinter());
			assertEquals("fileTypesScreen", config.getFileTypesScreen());
			assertEquals("dokart", config.getDokart());

		} catch (PdmDocumentsException e) {
			Assert.fail(e.getMessage());
		}

	}

	@Test
	public void testSetRestServer() {
		Configuration config = Configuration.getInstance();

		try {
			config.initConfiguration("localhost1", "restUser1", "restPassword1", "restTenant1", "partFieldName",
					"partProFileIDFieldName", UserEnumPdmSystems.KEYTECH, "sqlServer", 2222, "sqldatabase", "sqlDriver",
					"sqlUser", "sqlPassword", "fileTypesEmail", "fileTypesPrinter", "fileTypesScreen", "dokart");

			assertEquals("localhost1", config.getRestServer());
			assertEquals("restUser1", config.getRestUser());
			assertEquals("restPassword1", config.getRestPassword());
			assertEquals("restTenant1", config.getRestTenant());

			config.setRestServer("localhost2", "restUser2", "restPassword2", "restTenant2");

			assertEquals("localhost2", config.getRestServer());
			assertEquals("restUser2", config.getRestUser());
			assertEquals("restPassword2", config.getRestPassword());
			assertEquals("restTenant2", config.getRestTenant());

		} catch (PdmDocumentsException e) {
			Assert.fail(e.getMessage());
		}

	}

	@Test
	public void testSetSqlConnection() {
		Configuration config = Configuration.getInstance();
		try {
			config.initConfiguration("localhost", "restUser", "restPassword", "restTenant", "partFieldName",
					"partProFileIDFieldName", UserEnumPdmSystems.KEYTECH, "sqlServer", 2222, "sqldatabase", "sqlDriver",
					"sqlUser", "sqlPassword", "fileTypesEmail", "fileTypesPrinter", "fileTypesScreen", "dokart");

			assertEquals("sqlServer", config.getSqlServer());
			assertEquals("sqlUser", config.getSqlUser());
			assertEquals("sqlPassword", config.getSqlPassword());
			assertEquals("sqlDriver", config.getSqlDriver());
			assertEquals("sqldatabase", config.getSqldatabase());
			assertEquals(new Integer(2222), config.getSqlPort());

			config.setSqlConnection("sqlServer2", 3333, "sqldatabase2", "sqlUser2", "sqlPassword2", "sqlDriver2");

			assertEquals("sqlServer2", config.getSqlServer());
			assertEquals("sqlUser2", config.getSqlUser());
			assertEquals("sqlPassword2", config.getSqlPassword());
			assertEquals("sqlDriver2", config.getSqlDriver());
			assertEquals("sqldatabase2", config.getSqldatabase());
			assertEquals(new Integer(3333), config.getSqlPort());

		} catch (PdmDocumentsException e) {

			Assert.fail(e.getMessage());
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
		assertEquals(fileTypesEmailNew, config.getFileTypesEmail());
		assertNotEquals(fileTypesEmail, config.getFileTypesEmail());
		assertEquals(fileTypesScreenNew, config.getFileTypesScreen());
		assertNotEquals(fileTypesScreen, config.getFileTypesScreen());
		assertEquals(fileTypesPrinterNew, config.getFileTypesPrinter());
		assertNotEquals(fileTypesPrinter, config.getFileTypesPrinter());
	}

//	@Test
//	public void testGetRestServer() {
//		Configuration config = Configuration.getInstance();
//		config.setRestServer(restServer, restUser, restPassword, restTenant);
//		restserver = config.getRestServer();
//	}
//
//	@Test
//	public void testGetRestUser() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetRestPassword() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetRestTenant() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetPdmSystem() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetSqlServer() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetSqlPort() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetSqlPortString() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetSqlDriver() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetSqldatabase() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetSqlUser() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetSqlPassword() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetFileTypesEmail() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetFileTypesPrinter() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetFileTypesScreen() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSetPdmSystem() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetPartFieldName() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSetPartFieldName() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetPartProFileIDFieldName() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSetPartProFileIDFieldName() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSetSqlDriver() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetDokart() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSetDokart() {
//		fail("Not yet implemented");
//	}

}
