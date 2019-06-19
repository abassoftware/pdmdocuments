package de.abas.pdmdocuments.coffee.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import de.abas.pdmdocuments.coffee.infosystem.utils.DocumentsUtil;

public class DocumentsUtilsTest {

	@Test
	public void testGetPageFormat() {
		File testfilepdf = new File("test.pdf");
		File testfiletiff = new File("test.tif");
		String testpdf = null;
		String testtiff = null;
		try {
			testpdf = DocumentsUtil.getPageFormat(testfilepdf);
			testtiff = DocumentsUtil.getPageFormat(testfiletiff);
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
