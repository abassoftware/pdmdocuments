package de.abas.pdmdocuments.infosystem.data;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import de.abas.pdmdocuments.infosystem.data.DocMetaData;
import de.abas.pdmdocuments.infosystem.data.MetaDataTyp;

public class DocMetaDataTest {
	static DocMetaData docMetaTest;


	@BeforeAll
	public static void setUp() throws Exception {
		Object testintObj = (Object) new Integer(2);
		docMetaTest = new DocMetaData("inttest", testintObj);
	}


	@Test
	public void testDocMetaDataStringObject() {

		Object testintObj = (Object) new Integer(2);
		DocMetaData docMetaInt = new DocMetaData("int", testintObj);
		assertEquals("2", docMetaInt.getValue());
		assertEquals(MetaDataTyp.INTEGER, docMetaInt.getType());
		assertEquals("int", docMetaInt.getName());

		Object teststringObj = (Object) new String("test");
		DocMetaData docMetaString = new DocMetaData("String", teststringObj);
		assertEquals("test", docMetaString.getValue());
		assertEquals(MetaDataTyp.STRING, docMetaString.getType());
		assertEquals("String", docMetaString.getName());

		Date testDate = new Date();
		Object testDateObj = (Object) testDate;
		DocMetaData docMetaDate = new DocMetaData("Date", testDateObj);
		assertEquals(testDate.toString(), docMetaDate.getValue());
		assertEquals(MetaDataTyp.DATE, docMetaDate.getType());
		assertEquals("Date", docMetaDate.getName());

		Object testBigDecObj = (Object) new BigDecimal(2018);
		DocMetaData docMetaBigDec = new DocMetaData("BigDecimal", testBigDecObj);
		assertEquals("2018", docMetaBigDec.getValue());
		assertEquals(MetaDataTyp.BIGDEZIMAL, docMetaBigDec.getType());
		assertEquals("BigDecimal", docMetaBigDec.getName());

		Object testStringListObj = (Object) new String[] { "2018", "2019" };
		DocMetaData docMetaStringList = new DocMetaData("StringList", testStringListObj);
		assertEquals("20182019", docMetaStringList.getValue());
		assertEquals(MetaDataTyp.STRINGLIST, docMetaStringList.getType());
		assertEquals("StringList", docMetaStringList.getName());

	}

	@Test
	public void testDocMetaDataStringInteger() {
		Integer testintObj = new Integer(2);
		DocMetaData docMetaInt = new DocMetaData("int", testintObj);
		assertEquals("2", docMetaInt.getValue());
		assertEquals(MetaDataTyp.INTEGER, docMetaInt.getType());
		assertEquals("int", docMetaInt.getName());
	}

	@Test
	public void testDocMetaDataStringDate() {
		Date testDateObj = new Date();
		DocMetaData docMetaDate = new DocMetaData("Date", testDateObj);
		assertEquals(testDateObj.toString(), docMetaDate.getValue());
		assertEquals(MetaDataTyp.DATE, docMetaDate.getType());
		assertEquals("Date", docMetaDate.getName());

	}

	@Test
	public void testDocMetaDataStringBigDecimal() {
		BigDecimal testBigDecObj = new BigDecimal(2018);
		DocMetaData docMetaBigDec = new DocMetaData("BigDecimal", testBigDecObj);
		assertEquals("2018", docMetaBigDec.getValue());
		assertEquals(MetaDataTyp.BIGDEZIMAL, docMetaBigDec.getType());
		assertEquals("BigDecimal", docMetaBigDec.getName());
	}

	@Test
	public void testDocMetaDataStringString() {
		String teststringObj = new String("test");
		DocMetaData docMetaString = new DocMetaData("String", teststringObj);
		assertEquals("test", docMetaString.getValue());
		assertEquals(MetaDataTyp.STRING, docMetaString.getType());
		assertEquals("String", docMetaString.getName());
	}

	@Test
	public void testGetName() {
		assertEquals("inttest", docMetaTest.getName());
	}

	@Test
	public void testGetValue() {
		assertEquals("2", docMetaTest.getValue());

	}

	@Test
	public void testGetType() {

		assertEquals("inttest", docMetaTest.getName());
	}

}
