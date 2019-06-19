package de.abas.pdmdocuments.coffee.keytech;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.abas.erp.db.schema.userenums.UserEnumPdmSystems;
import de.abas.pdmdocuments.coffee.infosystem.DocumentSearchfactory;
import de.abas.pdmdocuments.coffee.infosystem.DocumentsInterface;
import de.abas.pdmdocuments.coffee.infosystem.PdmDocumentsException;
import de.abas.pdmdocuments.coffee.infosystem.config.Configuration;
import de.abas.pdmdocuments.coffee.infosystem.data.PdmDocument;

public class RestServiceKeytechTest {
	private DocumentsInterface restService;

	@Before
	public void prepareRestService() {

		Configuration config = Configuration.getInstance();
		config.setPdmSystem(UserEnumPdmSystems.KEYTECH);

		DocumentSearchfactory factory = new DocumentSearchfactory();

		try {
			config.setRestServer("https://demo.keytech.de", "jgrant", "", "");
			this.restService = factory.create(config);
		} catch (PdmDocumentsException e) {
			e.printStackTrace();
			fail(e.toString());
		}

	}

	@Test
	public void testSearchPdmProductID() {

		try {

			String ergebnis = this.restService.searchPdmProductID("BCU 4010 GP");
			assertTrue(ergebnis.length() > 0);
		} catch (PdmDocumentsException e) {
			e.printStackTrace();
			fail(e.toString());
		} catch (NullPointerException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void testGetAllDocuments() {

		String[] typFileList = new String().split(",");
		try {

			List<PdmDocument> ergebnis = this.restService.getAllDocuments("ITM004730", typFileList);
			assertTrue(ergebnis != null);
		} catch (PdmDocumentsException e) {
			e.printStackTrace();
			fail(e.toString());
		} catch (NullPointerException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	//
	// @Test
	// public void testGetAllDocumentsUnderThisProduct() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testRequestRestservicePDMProductID() {
	// fail("Not yet implemented");
	// }

}
