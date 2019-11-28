package de.abas.pdmdocuments.infosystem;

import de.abas.erp.db.schema.customer.Customer;
import de.abas.erp.db.schema.customer.CustomerContact;
import de.abas.erp.db.schema.customer.CustomerContactEditor;
import de.abas.erp.db.schema.customer.CustomerEditor;
import de.abas.erp.db.schema.customer.SelectableCustomer;
import de.abas.erp.db.schema.part.Product;
import de.abas.erp.db.schema.part.ProductEditor;
import de.abas.erp.db.schema.part.SelectablePart;
import de.abas.erp.db.schema.referencetypes.PurchasingAndSalesProcessEditor;
import de.abas.erp.db.schema.referencetypes.TradingPartner;
import de.abas.erp.db.schema.referencetypes.TradingPartnerEditor;
import de.abas.erp.db.schema.sales.BlanketOrderEditor;
import de.abas.erp.db.schema.sales.InvoiceEditor;
import de.abas.erp.db.schema.sales.OpportunityEditor;
import de.abas.erp.db.schema.sales.PackingSlipEditor;
import de.abas.erp.db.schema.sales.QuotationEditor;
import de.abas.erp.db.schema.sales.RepairOrderEditor;
import de.abas.erp.db.schema.sales.SalesOrderEditor;
import de.abas.erp.db.schema.sales.SelectableSales;
import de.abas.erp.db.schema.sales.ServiceQuotationEditor;
import de.abas.erp.db.schema.sales.WebOrderEditor;
import de.abas.erp.db.schema.sales.BlanketOrderEditor.Row;
import de.abas.erp.db.schema.vendor.Vendor;
import de.abas.erp.db.schema.vendor.VendorContact;
import de.abas.erp.db.schema.vendor.VendorContactEditor;
import de.abas.erp.db.schema.vendor.VendorEditor;
import de.abas.esdk.test.util.EsdkIntegTest;
import de.abas.esdk.test.util.TestData;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import static de.abas.pdmdocuments.infosystem.AbstractTest.CustomerData.CUSTOMER;
import static de.abas.pdmdocuments.infosystem.AbstractTest.VendorData.VENDOR;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import java.util.List;
import java.util.Random;

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



public abstract class AbstractTest extends EsdkIntegTest {
	
	@BeforeClass
	public static void prepare() {
		createTestDataCustomer(CUSTOMER);
		createTestDataVendor(VendorEditor.class, VENDOR);

		createTestDataProduct(ProductEditor.class, PRODUCT1);
		createTestDataProduct(ProductEditor.class, PRODUCT2);
		
		createTestDataSales(de.abas.erp.db.schema.sales.SalesOrderEditor.class, SALESORDER);

	}
	
	@AfterAll
	public static void cleanup() {
		TestData.deleteData(ctx, Customer.class, Customer.META.swd, CUSTOMER.swd);

	}
	
	
	private static <T extends PurchasingAndSalesProcessEditor> void createTestDataSales(Class<T> clazz, PDMDocumentsInfosystemTest.SalesData testData) {
		T editor = ctx.newObject(clazz);
		if (editor instanceof de.abas.erp.db.schema.sales.SalesOrderEditor) {
			createSalesOrder(testData, editor);
		}
		
		editor.commit();
		testData.sales = (SelectableSales) editor.getId();
	}

	private static <T extends PurchasingAndSalesProcessEditor> void createSalesOrder(
			PDMDocumentsInfosystemTest.SalesData testData, T editor) {
		((de.abas.erp.db.schema.sales.SalesOrderEditor) editor).setCustomer(testData.customer);	
		
		de.abas.erp.db.schema.sales.SalesOrderEditor.Row row = ((de.abas.erp.db.schema.sales.SalesOrderEditor) editor).table().appendRow();
		row.setProduct(testData.product1);
		row.setUnitQty(200);
		row.setPrice(20);
		
		de.abas.erp.db.schema.sales.SalesOrderEditor.Row row2 = ((de.abas.erp.db.schema.sales.SalesOrderEditor) editor).table().appendRow();
		row2.setProduct(testData.product2);
		row2.setUnitQty(200);
		row2.setPrice(20);
	}
	
	private static void createTestDataProduct(Class<ProductEditor> clazz, AbstractTest.ProductData testData) {
		ProductEditor editor = ctx.newObject(clazz);	
		editor.setSwd(testData.swd);
		editor.commit();
		testData.product = editor;
	}
	private static <T extends VendorEditor> void createTestDataVendor(Class<T> clazz, AbstractTest.VendorData testData) {
		VendorEditor editor = ctx.newObject(clazz);	
		editor.setSwd(testData.swd);
		editor.commit();
		testData.vendor = editor;
	}
	private static void createTestDataCustomer(PDMDocumentsInfosystemTest.CustomerData testData) {
		CustomerEditor editor = ctx.newObject(CustomerEditor.class);	
		editor.setSwd(testData.swd);
		editor.commit();
		testData.customer = editor;
	}
	
	
	enum ProductData {
		PRODUCT1("PDMProduct1"),
		PRODUCT2("PDMProduct2");

		String swd;
		Product product;

		ProductData(String swd) {
			this.swd = swd;
		}

	}
	
	enum VendorData {
		VENDOR("PDMVend");

		String swd;
		Vendor vendor;

		VendorData(String swd) {
			this.swd = swd;
		}

	}
	
	enum CustomerData {
		CUSTOMER("PDMCust");

		String swd;
		Customer customer;

		CustomerData(String swd) {
			this.swd = swd;
		}

	}
	
	enum SalesData {
		
		SALESORDER(CUSTOMER.customer, PRODUCT1.product, PRODUCT2.product),
		BLANKETORDER(CustomerData.CUSTOMER.customer, ProductData.PRODUCT1.product, ProductData.PRODUCT2.product),
		INVOICE(CustomerData.CUSTOMER.customer, ProductData.PRODUCT1.product, ProductData.PRODUCT2.product),
		PACKINGSLIP(CustomerData.CUSTOMER.customer, ProductData.PRODUCT1.product, ProductData.PRODUCT2.product),
		OPPORTUNITY(CustomerData.CUSTOMER.customer, ProductData.PRODUCT1.product, ProductData.PRODUCT2.product),
		QUOTATION(CustomerData.CUSTOMER.customer, ProductData.PRODUCT1.product, ProductData.PRODUCT2.product),
		REPAIRORDER(CustomerData.CUSTOMER.customer, ProductData.PRODUCT1.product, ProductData.PRODUCT2.product),
		SERVICEQUOTATION(CustomerData.CUSTOMER.customer, ProductData.PRODUCT1.product, ProductData.PRODUCT2.product),
		WEBORDER(CustomerData.CUSTOMER.customer, ProductData.PRODUCT1.product, ProductData.PRODUCT2.product);
			
		
		Customer customer;
		Product product1;
		Product product2;
		SelectableSales sales;


		SalesData(Customer customer, Product product1, Product product2) {
			System.out.println("SALES");

			System.out.println(CUSTOMER.customer);
			System.out.println(CustomerData.CUSTOMER.customer);
			System.out.println(customer);

			this.customer = customer;
			this.product1 = product1;
			this.product2 = product1;
		}

	}
	
	
}
