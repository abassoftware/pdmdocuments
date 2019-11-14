package de.abas.pdmdocuments.infosystem.data;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import de.abas.pdmdocuments.infosystem.PdmDocumentsException;
import de.abas.pdmdocuments.infosystem.utils.UtilwithAbasConnection;

public class PdmDocumentTest {
	private static PdmDocument pdmDocTest;
	private static final String PDM_DOCUMENT_META_DATA_LIST_DOUBLE_VALUE = "pdmDocument.metaDataList.doubleValue";


	@BeforeAll
	public static void setUp() throws Exception {
		pdmDocTest = new PdmDocument("test.pdf", "testdoku", "http:\\\\testserver:8990\\");
	}



	@Test
	public void testPdmDocumentFileString() {
		File testFile = new File("src/test/resources/base/owpdm/test.pdf");
		String docTyp = "testDoc";
		try {
			PdmDocument pdmDoc = new PdmDocument(testFile, docTyp);
			assertEquals("test.pdf", pdmDoc.getFilename());
			assertEquals("testDoc", pdmDoc.getDocumenttyp());
			assertEquals("pdf", pdmDoc.getFiletyp());
			Collection<DocMetaData> metaDataList = pdmDoc.getMetaDataList();
			assertNotEquals(null, metaDataList);
			assertTrue(metaDataList.isEmpty());
			assertEquals("A4", pdmDoc.getPageformat());
			assertEquals(null, pdmDoc.getUrlDocFile());

		} catch (PdmDocumentsException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testPdmDocumentStringStringString() {
		String filename = "test.tiff";
		PdmDocument pdmDoc = new PdmDocument(filename, "testdoku", "http:\\\\testserver:8990\\");
		assertEquals("test.tiff", pdmDoc.getFilename());
		assertEquals("testdoku", pdmDoc.getDocumenttyp());
		assertEquals("tiff", pdmDoc.getFiletyp());
		assertEquals("http:\\\\testserver:8990\\", pdmDoc.getUrlDocFile());

	}

	@Test
	public void testAddFile() {

	}

	@Test
	public void testGetUrlDocFile() {
		assertEquals("http:\\\\testserver:8990\\", this.pdmDocTest.getUrlDocFile());
	}

	@Test
	public void testGetDocumenttyp() {
		assertEquals("testdoku", this.pdmDocTest.getDocumenttyp());
	}

	@Test
	public void testAddDocMetaDataStringString() {
		try {
			this.pdmDocTest.addDocMetaData("testString", "String");

			assertEquals("String", this.pdmDocTest.getDocMetaDataByName("testString").getValue());

		} catch (PdmDocumentsException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testAddDocMetaDataStringInteger() {

		try {
			this.pdmDocTest.addDocMetaData("testInteger", new Integer(22));

			assertEquals("22", this.pdmDocTest.getDocMetaDataByName("testInteger").getValue());

		} catch (PdmDocumentsException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testAddDocMetaDataStringDate() {
		try {
			Date date = new Date();
			this.pdmDocTest.addDocMetaData("testDate", date);

			assertEquals(date.toString(), this.pdmDocTest.getDocMetaDataByName("testDate").getValue());

		} catch (PdmDocumentsException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testAddDocMetaDataStringBigDecimal() {
		String testname = "testBigDec";
		try {
	
			this.pdmDocTest.addDocMetaData("testBigDec", new BigDecimal(22));

			assertEquals("22", this.pdmDocTest.getDocMetaDataByName(testname).getValue());

		} catch (PdmDocumentsException e) {
			fail(e.getMessage());
		}

		try {
			this.pdmDocTest.addDocMetaData(testname, new BigDecimal(23));

		} catch (PdmDocumentsException e) {

			assertThat(e.getMessage(),
					is(UtilwithAbasConnection.getMessage(PDM_DOCUMENT_META_DATA_LIST_DOUBLE_VALUE, testname)));
		}
	}

	@Test
	public void testGetDocMetaDataByName() {
		try {

			this.pdmDocTest.addDocMetaData("testBigDec2", new BigDecimal(22));

			assertEquals("testBigDec2", this.pdmDocTest.getDocMetaDataByName("testBigDec2").getName());

		} catch (PdmDocumentsException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testAddDocMetaDataStringObject() {
		try {
			this.pdmDocTest.addDocMetaData("valueNameStringTest", "TestString");
			assertEquals("valueNameStringTest", this.pdmDocTest.getDocMetaDataByName("valueNameStringTest").getName());
		} catch (PdmDocumentsException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testHasFile() {
		assertFalse(this.pdmDocTest.hasFile());
		File testfile = new File("src/test/resources/base/owpdm/test.pdf");
		this.pdmDocTest.addFile(testfile);
		assertTrue(this.pdmDocTest.hasFile());
	}

	@Test
	public void testGetFile() {
		File file = this.pdmDocTest.getFile();
		if (this.pdmDocTest.hasFile()) {
			assertNotNull(file);
		}
		if (!this.pdmDocTest.hasFile()) {
			assertNull(file);
		}
		File testFile = new File("src/test/resources/base/owpdm/test.pdf");
		String docTyp = "testDoc";
		try {
			PdmDocument pdmDoc = new PdmDocument(testFile, docTyp);
			File testout = pdmDoc.getFile();
			assertEquals(testFile, testout);

		} catch (PdmDocumentsException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetPageformat() {
		File testFile = new File("src/test/resources/base/owpdm/test.pdf");
		String docTyp = "testDoc";
		try {
			PdmDocument pdmDoc = new PdmDocument(testFile, docTyp);
			assertEquals("A4", pdmDoc.getPageformat());
		} catch (PdmDocumentsException e) {
			fail(e.getMessage());
		}

		String filename = "test.tiff";
		PdmDocument pdmDocOHneFile = new PdmDocument(filename, "testdoku", "http:\\\\testserver:8990\\");
		assertNull(pdmDocOHneFile.getPageformat());

	}

	@Test
	public void testGetFilename() {
		String filename = "test.tiff";
		PdmDocument pdmDocOHneFile = new PdmDocument(filename, "testdoku", "http:\\\\testserver:8990\\");
		assertEquals("test.tiff", pdmDocOHneFile.getFilename());

		assertEquals("test.pdf", this.pdmDocTest.getFilename());
	}

	@Test
	public void testGetFiletyp() {
		String filename = "test.tiff";
		PdmDocument pdmDocOHneFile = new PdmDocument(filename, "testdoku", "http:\\\\testserver:8990\\");
		assertEquals("tiff", pdmDocOHneFile.getFiletyp());

		assertEquals("pdf", this.pdmDocTest.getFiletyp());

		String filenameOhneEndung = "test";
		PdmDocument pdmDocOHneEndung = new PdmDocument(filenameOhneEndung, "testdoku", "http:\\\\testserver:8990\\");
		assertEquals("", pdmDocOHneEndung.getFiletyp());
	}

	@Test
	public void testGetMetaDataList() {

		String filenameOhneEndung = "test";
		PdmDocument pdmDocMetaData = new PdmDocument(filenameOhneEndung, "testdoku", "http:\\\\testserver:8990\\");
		try {
			Collection<DocMetaData> metaDataListEmpty = pdmDocMetaData.getMetaDataList();
			assertTrue(metaDataListEmpty.isEmpty());
			assertEquals(0, metaDataListEmpty.size());
			pdmDocMetaData.addDocMetaData("testString", "String");
			pdmDocMetaData.addDocMetaData("testString2", "String2");
			pdmDocMetaData.addDocMetaData("testString3", "String3");
			Collection<DocMetaData> metaDataList = pdmDocMetaData.getMetaDataList();
			assertEquals(3, metaDataList.size());

		} catch (PdmDocumentsException e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testGetError() {

		String filenameOhneEndung = "test";
		PdmDocument pdmDocError = new PdmDocument(filenameOhneEndung, "testdoku", "http:\\\\testserver:8990\\");
		String errortxt = "Endung fehlt";
		pdmDocError.addError(errortxt);
		assertEquals(errortxt, pdmDocError.getError());

		String errortest2 = "Fehler2";

		pdmDocError.addError(errortest2);

		assertEquals(errortxt + ";" + errortest2, pdmDocError.getError());

	}

	@Test
	public void testHasError() {
		String filenameOhneEndung = "test";
		PdmDocument pdmDocError = new PdmDocument(filenameOhneEndung, "testdoku", "http:\\\\testserver:8990\\");
		String errortxt = "Endung fehlt";
		assertFalse(pdmDocError.hasError());
		pdmDocError.addError(errortxt);
		assertTrue(pdmDocError.hasError());

	}

	@Test
	public void testCheckFileListTyp() {
		String filename = "test.tiff";
		PdmDocument pdmDoc = new PdmDocument(filename, "testdoku", "http:\\\\testserver:8990\\");
		String[] typListMitTreffer = new String[] { "tiff", "doc", "pdf" };
		String[] typListOhneTreffer = new String[] { "doc", "pdf" };
		String[] typListMitTrefferCapital = new String[] { "TIFF", "DOC", "PDF" };
		String[] typListOhneTrefferKurz = new String[] { "tif", "doc", "pdf" };
		String[] typListLeer = null;

		assertTrue(pdmDoc.checkFileListTyp(typListMitTreffer));
		assertTrue(pdmDoc.checkFileListTyp(typListMitTrefferCapital));
		assertFalse(pdmDoc.checkFileListTyp(typListOhneTreffer));
		assertFalse(pdmDoc.checkFileListTyp(typListOhneTrefferKurz));
		assertTrue(pdmDoc.checkFileListTyp(typListLeer));

	}

	@Test
	public void testCheckFileNameList() {
		String filename = "test.tiff";
		PdmDocument pdmDoc = new PdmDocument(filename, "testdoku", "http:\\\\testserver:8990\\");
		List<String> nameListMitTreffer = new ArrayList<String>();
		nameListMitTreffer.add("test.tiff");
		nameListMitTreffer.add("versuch");
		nameListMitTreffer.add("datei");
		List<String> nameListOhneTreffer = new ArrayList<String>();
		nameListOhneTreffer.add("versuch");
		nameListOhneTreffer.add("datei");
		List<String> nameListMitTrefferCapital = new ArrayList<String>();
		nameListMitTrefferCapital.add("TEST.TIFF");
		nameListMitTrefferCapital.add("VERSUCH");
		nameListMitTrefferCapital.add("DATEI");

		List<String> nameListOhneTrefferKurz = new ArrayList<String>();
		nameListOhneTrefferKurz.add("tes.tiff");
		nameListOhneTrefferKurz.add("versuch");
		nameListOhneTrefferKurz.add("datei");
		List<String> nameListLeer = null;

		assertTrue(pdmDoc.checkFileNameList(nameListMitTreffer));
		assertTrue(pdmDoc.checkFileNameList(nameListMitTrefferCapital));
		assertFalse(pdmDoc.checkFileNameList(nameListOhneTreffer));
		assertFalse(pdmDoc.checkFileNameList(nameListOhneTrefferKurz));
		assertTrue(pdmDoc.checkFileNameList(nameListLeer));
	}

	@Test
	public void testCheckDocTypList() {
		String filename = "test.tiff";
		PdmDocument pdmDoc = new PdmDocument(filename, "testdoku", "http:\\\\testserver:8990\\");
		List<String> docTypListMitTreffer = new ArrayList<String>();
		docTypListMitTreffer.add("testdoku");
		docTypListMitTreffer.add("versuch");
		docTypListMitTreffer.add("datei");
		List<String> doctypListOhneTreffer = new ArrayList<String>();
		doctypListOhneTreffer.add("versuch");
		doctypListOhneTreffer.add("datei");
		List<String> docTypListMitTrefferCapital = new ArrayList<String>();
		docTypListMitTrefferCapital.add("TESTDOKU");
		docTypListMitTrefferCapital.add("VERSUCH");
		docTypListMitTrefferCapital.add("DATEI");

		List<String> docTypListOhneTrefferKurz = new ArrayList<String>();
		docTypListOhneTrefferKurz.add("test");
		docTypListOhneTrefferKurz.add("versuch");
		docTypListOhneTrefferKurz.add("datei");
		List<String> docTypListLeer = null;

		assertTrue(pdmDoc.checkDocTypList(docTypListMitTreffer));
		assertTrue(pdmDoc.checkDocTypList(docTypListMitTrefferCapital));
		assertFalse(pdmDoc.checkDocTypList(doctypListOhneTreffer));
		assertFalse(pdmDoc.checkDocTypList(docTypListOhneTrefferKurz));
		assertTrue(pdmDoc.checkDocTypList(docTypListLeer));
	}

}
