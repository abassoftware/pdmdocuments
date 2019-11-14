package de.abas.pdmdocuments.infosystem.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import de.abas.pdmdocuments.infosystem.utils.DocumentsUtil;

public class DocumentsUtilsTest {

	@Test
	public void testGetPageFormat() {
		File testfilepdf = new File("src/test/resources/base/owpdm/test.pdf");
		File testfiletiff = new File("src/test/resources/base/owpdm/test.tif");
		String testpdf = null;
		String testtiff = null;
		try {
			testpdf = DocumentsUtil.getPageFormat(testfilepdf);
			testtiff = DocumentsUtil.getPageFormat(testfiletiff);
			assertEquals(testpdf, "A4");
			assertEquals(testtiff, "SO");
		} catch (IOException e) {
			fail(e.getMessage());

		}

		assertTrue(testtiff != null && testpdf != null && !testpdf.isEmpty() && !testtiff.isEmpty());

	}

	@Test
	public void testgetFileExtensionFromName() {
		String input1 = "test.tif";
		String input2 = "test";
		String input3 = "test;tif";
		String output1 = DocumentsUtil.getFileExtensionFromName(input1);
		String output2 = DocumentsUtil.getFileExtensionFromName(input2);
		String output3 = DocumentsUtil.getFileExtensionFromName(input3);

		assertEquals("tif", output1);
		assertEquals("", output2);
		assertEquals("", output3);

	}

}
