package de.abas.pdmdocuments.infosystem;

<<<<<<< HEAD
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.abas.erp.db.SelectableObject;
import de.abas.erp.db.infosystem.custom.owpdm.PdmDocuments;
import de.abas.erp.db.schema.customer.CustomerEditor;
import de.abas.erp.db.schema.part.Product;
import de.abas.erp.db.schema.part.ProductEditor;
import de.abas.erp.db.schema.part.SelectablePart;
import de.abas.erp.db.schema.vendor.VendorEditor;
import de.abas.esdk.test.util.EsdkIntegTest;


import static de.abas.pdmdocuments.infosystem.AbstractTest.CustomerData.CUSTOMER;
import static de.abas.pdmdocuments.infosystem.AbstractTest.VendorData.VENDOR;


import static de.abas.pdmdocuments.infosystem.AbstractTest.ProductData.PRODUCT1;
import static de.abas.pdmdocuments.infosystem.AbstractTest.ProductData.PRODUCT2;

import static de.abas.pdmdocuments.infosystem.AbstractTest.SalesData.SALESORDER;
import static de.abas.pdmdocuments.infosystem.AbstractTest.SalesData.BLANKETORDER;
import static de.abas.pdmdocuments.infosystem.AbstractTest.SalesData.INVOICE;
import static de.abas.pdmdocuments.infosystem.AbstractTest.SalesData.PACKINGSLIP;
import static de.abas.pdmdocuments.infosystem.AbstractTest.SalesData.OPPORTUNITY;
import static de.abas.pdmdocuments.infosystem.AbstractTest.SalesData.QUOTATION;
import static de.abas.pdmdocuments.infosystem.AbstractTest.SalesData.REPAIRORDER;
import static de.abas.pdmdocuments.infosystem.AbstractTest.SalesData.SERVICEQUOTATION;
import static de.abas.pdmdocuments.infosystem.AbstractTest.SalesData.WEBORDER;


public class PDMDocumentsInfosystemTest extends AbstractTest {
	
	public PdmDocuments infosystem = ctx.openInfosystem(PdmDocuments.class);
	
	@Test
	public void canDisplayCustomerInfo() {
		System.out.println("TEST");

		System.out.println(CUSTOMER);
		System.out.println(CUSTOMER.swd);
		System.out.println(CUSTOMER.customer);
		
		System.out.println(CustomerData.CUSTOMER.customer);

		
		
		
		// assertNotNull(CustomerData.CUSTOMER.customer);
	}
	
	/*public PdmDocuments infosystem = ctx.openInfosystem(PdmDocuments.class);
	
	CreateSalesTestData salesData = new CreateSalesTestData();
	CreatePurchasingTestData purchsasingData = new CreatePurchasingTestData();
	CreateProductsTestData productsData = new CreateProductsTestData();
	

	List<SelectableObject> salesObject = salesData.createSalesData();
	List<SelectableObject> purchasingObject = purchsasingData.createPurchsasingData();
	List<SelectablePart> products = productsData.createProducts();
	
	
	@Test
	public void canDisplayCustomerInfo() {
		
		infosystem.setYartikel( (Product) products.get(0));
		infosystem.setYbeleg(salesObject.get(0));
		infosystem.invokeStart();

		assertTrue((infosystem.table().getRowCount()) > 0);

		
	}
	
	private void assertInfosystemTableContains() {
		assertTrue((infosystem.table().getRowCount()) > 0);
		/*
		for (int i = 0; i < testData.length; i++) {
			int rowNo = i + 1;
			assertThat(infosystem.table().getRow(rowNo).getCustomer(), is(notNullValue()));
			assertThat(infosystem.table().getRow(rowNo).getCustomer().getSwd(), is(testData[i].swd));
			assertThat(infosystem.table().getRow(rowNo).getZipcode(), is(testData[i].zipCode));
			assertThat(infosystem.table().getRow(rowNo).getTown(), is(testData[i].town));
		}
		
	}
	*/
=======
public class PDMDocumentsInfosystemTest {

	// public PDMDocuments infosystem = ctx.openInfosystem(PDMDocuments.class);

>>>>>>> e542106c4509fb2ac220d193794e19c8a275c326
}
