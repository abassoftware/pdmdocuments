package de.abas.pdmdocuments.infosystem;

import static de.abas.pdmdocuments.infosystem.AbstractTest.CustomerData.CUSTOMER;

import org.junit.jupiter.api.Test;

import de.abas.erp.db.infosystem.custom.owpdm.PdmDocuments;

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

	/*
	 * public PdmDocuments infosystem = ctx.openInfosystem(PdmDocuments.class);
	 * 
	 * CreateSalesTestData salesData = new CreateSalesTestData();
	 * CreatePurchasingTestData purchsasingData = new CreatePurchasingTestData();
	 * CreateProductsTestData productsData = new CreateProductsTestData();
	 * 
	 * 
	 * List<SelectableObject> salesObject = salesData.createSalesData();
	 * List<SelectableObject> purchasingObject =
	 * purchsasingData.createPurchsasingData(); List<SelectablePart> products =
	 * productsData.createProducts();
	 * 
	 * 
	 * @Test public void canDisplayCustomerInfo() {
	 * 
	 * infosystem.setYartikel( (Product) products.get(0));
	 * infosystem.setYbeleg(salesObject.get(0)); infosystem.invokeStart();
	 * 
	 * assertTrue((infosystem.table().getRowCount()) > 0);
	 * 
	 * 
	 * }
	 * 
	 * private void assertInfosystemTableContains() {
	 * assertTrue((infosystem.table().getRowCount()) > 0); /* for (int i = 0; i <
	 * testData.length; i++) { int rowNo = i + 1;
	 * assertThat(infosystem.table().getRow(rowNo).getCustomer(),
	 * is(notNullValue()));
	 * assertThat(infosystem.table().getRow(rowNo).getCustomer().getSwd(),
	 * is(testData[i].swd));
	 * assertThat(infosystem.table().getRow(rowNo).getZipcode(),
	 * is(testData[i].zipCode));
	 * assertThat(infosystem.table().getRow(rowNo).getTown(), is(testData[i].town));
	 * }
	 * 
	 * }
	 */

}
