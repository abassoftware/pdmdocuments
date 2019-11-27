package de.abas.pdmdocuments.infosystem.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import de.abas.eks.jfop.annotation.RunFop;

public class UtilTest {



	@RunFop
	@Test
	public void testGetMessageString() {
		assertEquals("\"Fehler\"", Util.getMessage("main.exception.title", Locale.GERMAN));
	}

	@Test
	public void testGetMessageStringNullpointer() {
		// FIXME: Es darf keine Exception geworfen werden
		String testnull = null;
		assertNull(Util.getMessage(testnull, Locale.GERMAN));
	}

	@Test
	public void testGetMessageStringObjectArray() {

		assertEquals("\"Keine Verbindug zu Server Server1 m�glich\"",
				Util.getMessage("main.error.noConnection", Locale.GERMAN, "Server1"));

	}

	@Test
	public void testGetTimestamp() {
		assertNotNull(Util.getTimestamp());
		String timestamp = Util.getTimestamp();
		try {
			Long test = new Long(timestamp);
			long jetzt = Instant.now().toEpochMilli();
			if (jetzt < test) {
				fail("Die jetzige Zeit ist kleiner als die Testzeit");
			}
		} catch (NumberFormatException e) {
			fail("Übergebene Anzahl millisekunden kann nicht zurückumgewandelt werden.");
		}

	}

	@Test
	public void testReplaceUmlaute() {
		assertEquals("Getraenk", Util.replaceUmlaute("Getränk"));
		assertEquals("Getraenk", Util.replaceUmlaute("Getraenk"));
		assertEquals("aeueoeAeUeOess", Util.replaceUmlaute("äüöÄÜÖß"));
		assertEquals("", Util.replaceUmlaute(""));
		try {
			assertEquals(null, Util.replaceUmlaute(null));
			fail("NullpointerException erwartet");
		} catch (NullPointerException e) {

		}
	}

	@Test
	public void testReplaceSonderzeichen() {
		assertEquals("Getr_nk", Util.replaceSonderzeichen("Getr/nk"));
		assertEquals("Getr_aenk", Util.replaceSonderzeichen("Getr aenk"));
		assertEquals("Getr_aenk", Util.replaceSonderzeichen("Getr;aenk"));
		assertEquals("Getr_aenk", Util.replaceSonderzeichen("Getr\\aenk"));
		assertEquals("Getr_aenk", Util.replaceSonderzeichen("Getr=aenk"));
		assertEquals("Getr_ae_nk", Util.replaceSonderzeichen("Getr=ae;nk"));
		assertEquals("", Util.replaceSonderzeichen(""));
		assertEquals("test_Test", Util.replaceSonderzeichen("testøTest"));
		try {
			assertEquals(null, Util.replaceSonderzeichen(null));
			fail("NullpointerException erwartet");
		} catch (NullPointerException e) {

		}
	}

	@Test
	public void testReplaceZeichen() {
		String[][] testarray = { { ";", "_" }, { "=", "_" } };
		String[][] testarray2 = { { "=", "_" } };
		String[][] testarray3 = {};
		assertEquals("Getr_ae_nk", Util.replaceZeichen("Getr=ae;nk", testarray));
		assertEquals("Getr_aenk", Util.replaceZeichen("Getr=aenk", testarray));
		assertEquals("Getr/aenk", Util.replaceZeichen("Getr/aenk", testarray));
		assertEquals("Getr_aenk", Util.replaceZeichen("Getr=aenk", testarray2));
		assertEquals("Getr_ae;nk", Util.replaceZeichen("Getr=ae;nk", testarray2));
		assertEquals("Getr=ae;nk", Util.replaceZeichen("Getr=ae;nk", testarray3));

	}

	@Test
	public void testReadStringFromFile() {
		File file = null;

		try {
			file = File.createTempFile("test", "txt");
			// leeres TempFile auslesen
			assertEquals("", Util.readStringFromFile(file));

			// Temp-File mit zeilenumbruch erstellen und wieder auslesen

			String filenameStringOut = "test" + System.getProperty("line.sperator") + "zeile2";
			Util.writeStringtoFile(file, filenameStringOut);
			assertEquals(filenameStringOut, Util.readStringFromFile(file));

		} catch (IOException e) {
			fail(e.getMessage());
			e.printStackTrace();
		} finally {
			if (file != null) {
				file.delete();
			}
		}

	}

	@Test
	public void testReadStringListFromFile() {
		File file = null;
		// leeres TempFile auslesen
		try {
			file = File.createTempFile("test", "txt");
			List<String> emptyList = new ArrayList<>();
			assertEquals(emptyList, Util.readStringListFromFile(file));

			// Temp-File mit zeilenumbruch erstellen und wieder auslesen
			List<String> shortList = new ArrayList<>();
			shortList.add("test");
			shortList.add("zeile2");

			Util.writeStringtoFile(file, shortList);
			assertEquals(shortList, Util.readStringListFromFile(file));

		} catch (IOException e) {
			fail(e.getMessage());
			e.printStackTrace();
		} finally {
			if (file != null) {

				try {
					Files.delete(file.toPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

//	@Test
//	public void testWriteStringtoFileFileString() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testWriteStringtoFileFileArrayListOfString() {
//		fail("Not yet implemented");
//	}

	@Test
	public void testGettempFile() {
		File testfile = null;
		try {
			testfile = Util.gettempFile(".", "Xtest", "TMP");
			assert (testfile.exists());
			assert (testfile.canWrite());
			assert (testfile.canRead());
			assert (testfile.isFile());
			assertEquals(0, testfile.length());
		} catch (IOException e) {
			fail(e.getMessage());
			e.printStackTrace();
		} finally {
			if (testfile != null) {
				try {
					Files.delete(testfile.toPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
