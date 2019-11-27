package de.abas.pdmdocuments.infosystem;


import de.abas.erp.db.SelectableObject;
import de.abas.erp.db.schema.customer.Customer;
import de.abas.erp.db.schema.customer.CustomerEditor;
import de.abas.erp.db.schema.customer.SelectableCustomer;
import de.abas.erp.db.schema.part.ProductEditor;
import de.abas.erp.db.schema.part.SelectablePart;
import de.abas.erp.db.schema.sales.*;
import de.abas.erp.db.schema.sales.SalesOrderEditor.Row;


import de.abas.erp.db.schema.referencetypes.PurchasingAndSalesProcessEditor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import de.abas.pdmdocuments.infosystem.PDMDocumentsInfosystemTest;
import de.abas.esdk.test.util.EsdkIntegTest;
import de.abas.esdk.test.util.TestData;


public class CreateSalesTestData extends EsdkIntegTest {

	@BeforeClass
	public List<SelectableObject> prepare() {
		List<SelectableObject> salesObject = new ArrayList<SelectableObject>();
		//create customer
		SelectableCustomer customer =createCustomer();		
		//create products
	    String[] productsSwd = {"PDMDOC1", "PDMDOC2"};
	    List<SelectablePart> products = new ArrayList<SelectablePart>();
	    for (String s: productsSwd)
        {
	    	products.add(createProducts(s));
        }
		// create sales
	    salesObject.add(createSalesObject(SalesOrderEditor.class ,customer, products));
	    salesObject.add(createSalesObject(BlanketOrderEditor.class ,customer, products));
	    salesObject.add(createSalesObject(InvoiceEditor.class ,customer, products));
	    salesObject.add(createSalesObject(PackingSlipEditor.class ,customer,  products));
	    salesObject.add(createSalesObject(OpportunityEditor.class ,customer, products));
	    salesObject.add(createSalesObject(QuotationEditor.class ,customer,  products));
	    salesObject.add(createSalesObject(RepairOrderEditor.class ,customer, products));
	    salesObject.add(createSalesObject(ServiceQuotationEditor.class ,customer,  products));
	    salesObject.add(createSalesObject(WebOrderEditor.class ,customer, products));
	    
	    return salesObject;
	}


	private static <T extends PurchasingAndSalesProcessEditor> SelectableObject createSalesObject(Class<T> clazz, SelectableCustomer customer, List<SelectablePart> products) {
			
		T editor = ctx.newObject(clazz);
		// Sales
		if (editor instanceof SalesOrderEditor) {
	 	    createSalesOrder(customer, products, editor);
		} else if (editor instanceof de.abas.erp.db.schema.sales.BlanketOrder) {
			createSalesBlanketOrder(customer, products, editor);
		} else if (editor instanceof de.abas.erp.db.schema.sales.Invoice) {
			createSalesInvoice(customer, products, editor);
		} else if (editor instanceof de.abas.erp.db.schema.sales.PackingSlip) {
			createSalesPackingSlip(customer, products, editor);
		}else if (editor instanceof Opportunity) {
			createSalesOpportunity(customer, products, editor);
		}else if (editor instanceof Quotation) {
			crateSalesQuotation(customer, products, editor);
		}else if (editor instanceof RepairOrder) {
			createSalesRepairOrder(customer, products, editor);
		}else if (editor instanceof ServiceQuotation) {
			createSalesServiceQuotation(customer, products, editor);
		}else if (editor instanceof WebOrder) {
			createSalesWebOder(customer, products, editor);
		}else if (editor instanceof ServiceOrder) {
			// TODO
			/*
			((ServiceOrderEditor) editor).setCustomer(customer);	
			for(SelectablePart prod : products) {
				de.abas.erp.db.schema.sales.ServiceOrderEditor.Row row = ((ServiceOrderEditor) editor).table().appendRow();
				row.setProduct(prod);
			    row.setUnitQty(new Random().nextDouble());
			    row.setPrice(new Random().nextDouble());
			    }
			    */
		}
					
		editor.commit();
		SelectableObject object = editor.getId();
		return object;
	}


	private static <T extends PurchasingAndSalesProcessEditor> void createSalesWebOder(SelectableCustomer customer,
			List<SelectablePart> products, T editor) {
		((WebOrderEditor) editor).setCustomer(customer);	
		for(SelectablePart prod : products) {
			de.abas.erp.db.schema.sales.WebOrderEditor.Row row = ((WebOrderEditor) editor).table().appendRow();
			row.setProduct(prod);
		    row.setUnitQty(new Random().nextInt(1000));
		    row.setPrice(new Random().nextDouble());
		    }
	}

	private static <T extends PurchasingAndSalesProcessEditor> void createSalesServiceQuotation(SelectableCustomer customer,
			List<SelectablePart> products, T editor) {
		((ServiceQuotationEditor) editor).setCustomer(customer);	
		for(SelectablePart prod : products) {
			de.abas.erp.db.schema.sales.ServiceQuotationEditor.Row row = ((ServiceQuotationEditor) editor).table().appendRow();
			row.setProduct(prod);
		    row.setUnitQty(new Random().nextInt(1000));
		    row.setPrice(new Random().nextDouble());
		    }
	}

	private static <T extends PurchasingAndSalesProcessEditor> void createSalesRepairOrder(SelectableCustomer customer,
			List<SelectablePart> products, T editor) {
		((RepairOrderEditor) editor).setCustomer(customer);	
		for(SelectablePart prod : products) {
			de.abas.erp.db.schema.sales.RepairOrderEditor.Row row = ((RepairOrderEditor) editor).table().appendRow();
			row.setProduct(prod);
		    row.setUnitQty(new Random().nextInt(1000));
		    row.setPrice(new Random().nextDouble());
		    }
	}

	private static <T extends PurchasingAndSalesProcessEditor> void crateSalesQuotation(SelectableCustomer customer,
			List<SelectablePart> products, T editor) {
		((QuotationEditor) editor).setCustomer(customer);	
		for(SelectablePart prod : products) {
			de.abas.erp.db.schema.sales.QuotationEditor.Row row = ((QuotationEditor) editor).table().appendRow();
			row.setProduct(prod);
		    row.setUnitQty(new Random().nextInt(1000));
		    row.setPrice(new Random().nextDouble());
		    }
	}

	private static <T extends PurchasingAndSalesProcessEditor> void createSalesOpportunity(SelectableCustomer customer,
			List<SelectablePart> products, T editor) {
		((OpportunityEditor) editor).setCustomer(customer);	
		for(SelectablePart prod : products) {
			de.abas.erp.db.schema.sales.OpportunityEditor.Row row = ((OpportunityEditor) editor).table().appendRow();
			row.setProduct(prod);
		    row.setUnitQty(new Random().nextInt(1000));
		    row.setPrice(new Random().nextDouble());
		    }
	}

	private static <T extends PurchasingAndSalesProcessEditor> void createSalesPackingSlip(SelectableCustomer customer,
			List<SelectablePart> products, T editor) {
		((de.abas.erp.db.schema.sales.PackingSlipEditor) editor).setCustomer(customer);	
		for(SelectablePart prod : products) {
			de.abas.erp.db.schema.sales.PackingSlipEditor.Row row = ((de.abas.erp.db.schema.sales.PackingSlipEditor) editor).table().appendRow();
			row.setProduct(prod);
		    row.setUnitQty(new Random().nextInt(1000));
		    row.setPrice(new Random().nextDouble());
		    }
	}

	private static <T extends PurchasingAndSalesProcessEditor> void createSalesInvoice(SelectableCustomer customer,
			List<SelectablePart> products, T editor) {
		((de.abas.erp.db.schema.sales.InvoiceEditor) editor).setCustomer(customer);	
		for(SelectablePart prod : products) {
			de.abas.erp.db.schema.sales.InvoiceEditor.Row row = ((de.abas.erp.db.schema.sales.InvoiceEditor) editor).table().appendRow();
			row.setProduct(prod);
		    row.setUnitQty(new Random().nextInt(1000));
		    row.setPrice(new Random().nextDouble());
		    }
	}

	private static <T extends PurchasingAndSalesProcessEditor> void createSalesBlanketOrder(SelectableCustomer customer,
			List<SelectablePart> products, T editor) {
		((de.abas.erp.db.schema.sales.BlanketOrderEditor) editor).setCustomer(customer);	
		for(SelectablePart prod : products) {
			de.abas.erp.db.schema.sales.BlanketOrderEditor.Row row = ((de.abas.erp.db.schema.sales.BlanketOrderEditor) editor).table().appendRow();
			row.setProduct(prod);
		    row.setUnitQty(new Random().nextInt(1000));
		    row.setPrice(new Random().nextDouble());
		    }
	}

	private static <T extends PurchasingAndSalesProcessEditor> void createSalesOrder(SelectableCustomer customer,
			List<SelectablePart> products, T editor) {
		((SalesOrderEditor) editor).setCustomer(customer);	
		for(SelectablePart prod : products) {
			Row row = ((SalesOrderEditor) editor).table().appendRow();
			row.setProduct(prod);
		    row.setUnitQty(new Random().nextInt(1000));
		    row.setPrice(new Random().nextDouble());
		}
	}


	private static SelectablePart createProducts(String product) {
		ProductEditor editor = ctx.newObject(ProductEditor.class);
		editor.setSwd(product);
		editor.commit();
		SelectablePart part = editor.getId();
		return part;
	}

	private static SelectableCustomer createCustomer() {
		CustomerEditor editor = ctx.newObject(CustomerEditor.class);
		editor.setSwd("PDMCust");
		editor.commit();
		SelectableCustomer customer = editor.getId();
		return customer;
	}

	
}
