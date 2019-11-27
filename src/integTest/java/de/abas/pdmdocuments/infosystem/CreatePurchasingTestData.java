package de.abas.pdmdocuments.infosystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.BeforeClass;

import de.abas.erp.db.SelectableObject;
import de.abas.erp.db.schema.customer.CustomerEditor;
import de.abas.erp.db.schema.customer.SelectableCustomer;
import de.abas.erp.db.schema.part.ProductEditor;
import de.abas.erp.db.schema.part.SelectablePart;
import de.abas.erp.db.schema.purchasing.BlanketOrder;
import de.abas.erp.db.schema.purchasing.BlanketOrderEditor;
import de.abas.erp.db.schema.purchasing.Invoice;
import de.abas.erp.db.schema.purchasing.InvoiceEditor;
import de.abas.erp.db.schema.purchasing.PackingSlip;
import de.abas.erp.db.schema.purchasing.PackingSlipEditor;
import de.abas.erp.db.schema.purchasing.PurchaseOrder;
import de.abas.erp.db.schema.purchasing.PurchaseOrderEditor;
import de.abas.erp.db.schema.purchasing.Request;
import de.abas.erp.db.schema.purchasing.RequestEditor;
import de.abas.erp.db.schema.referencetypes.PurchasingAndSalesProcessEditor;
import de.abas.erp.db.schema.vendor.SelectableVendor;
import de.abas.erp.db.schema.vendor.VendorEditor;
import de.abas.esdk.test.util.EsdkIntegTest;

public class CreatePurchasingTestData extends EsdkIntegTest {
	
	@BeforeClass
	public List<SelectableObject> prepare() {
		List<SelectableObject> purchasingObject = new ArrayList<SelectableObject>();
		//create vendor
		SelectableVendor vendor =createVendor();
		
		//create products
	    String[] productsSwd = {"PDMDOC3", "PDMDOC4"};
	    List<SelectablePart> products = new ArrayList<SelectablePart>();
	    for (String s: productsSwd)
        {
	    	products.add(createProducts(s));
        }

	    // create purchasing
	    purchasingObject.add(createPurchasingObject(BlanketOrderEditor.class , vendor, products));
	    purchasingObject.add(createPurchasingObject(InvoiceEditor.class , vendor, products));
	    purchasingObject.add(createPurchasingObject(PackingSlipEditor.class , vendor, products));
	    purchasingObject.add(createPurchasingObject(PurchaseOrderEditor.class , vendor, products));
	    purchasingObject.add(createPurchasingObject(RequestEditor.class, vendor, products));

	    return purchasingObject;
	}
	private static <T extends PurchasingAndSalesProcessEditor> SelectableObject createPurchasingObject(Class<T> clazz, SelectableVendor vendor,List<SelectablePart> products) {
		
		T editor = ctx.newObject(clazz);
		// Purchasing
	     if (editor instanceof BlanketOrder) {		
			createPurchsasingBlanketOrder(vendor, products, editor);
		}else if (editor instanceof Invoice) {
			createPurchasingInvoice(vendor, products, editor);				
		}else if (editor instanceof PackingSlip) {
			createPurchasingPackingSlip(vendor, products, editor);
		}else if (editor instanceof PurchaseOrder) {
			createPurchaseOrder(vendor, products, editor);
		}else if (editor instanceof Request) {
			createPurchaseRequest(vendor, products, editor);
		}
					
		editor.commit();
		SelectableObject object = editor.getId();
		return object;
	}



	private static <T extends PurchasingAndSalesProcessEditor> void createPurchsasingBlanketOrder(SelectableVendor vendor,
			List<SelectablePart> products, T editor) {
		((BlanketOrderEditor) editor).setVendor(vendor);	
		for(SelectablePart prod : products) {
			BlanketOrderEditor.Row row = ((BlanketOrderEditor) editor).table().appendRow();
			row.setProduct(prod);
		    row.setUnitQty(new Random().nextInt(1000));
		    row.setPrice(new Random().nextDouble());
		    }
	}

	private static <T extends PurchasingAndSalesProcessEditor> void createPurchaseRequest(SelectableVendor vendor,
			List<SelectablePart> products, T editor) {
		((RequestEditor) editor).setVendor(vendor);	
		for(SelectablePart prod : products) {
			RequestEditor.Row row = ((RequestEditor) editor).table().appendRow();
			row.setProduct(prod);
		    row.setUnitQty(new Random().nextInt(1000));
		    row.setPrice(new Random().nextDouble());
		    }
	}

	private static <T extends PurchasingAndSalesProcessEditor> void createPurchasingPackingSlip(SelectableVendor vendor,
			List<SelectablePart> products, T editor) {
		((PackingSlipEditor) editor).setVendor(vendor);	
		for(SelectablePart prod : products) {
			PackingSlipEditor.Row row = ((PackingSlipEditor) editor).table().appendRow();
			row.setProduct(prod);
		    row.setUnitQty(new Random().nextInt(1000));
		    row.setPrice(new Random().nextDouble());
		    }
	}

	private static <T extends PurchasingAndSalesProcessEditor> void createPurchaseOrder(SelectableVendor vendor,
			List<SelectablePart> products, T editor) {
		((PurchaseOrderEditor) editor).setVendor(vendor);	
		for(SelectablePart prod : products) {
			PurchaseOrderEditor.Row row = ((PurchaseOrderEditor) editor).table().appendRow();
			row.setProduct(prod);
		    row.setUnitQty(new Random().nextInt(1000));
		    row.setPrice(new Random().nextDouble());
		    }
	}

	private static <T extends PurchasingAndSalesProcessEditor> void createPurchasingInvoice(SelectableVendor vendor,
			List<SelectablePart> products, T editor) {
		((InvoiceEditor) editor).setVendor(vendor);	
		for(SelectablePart prod : products) {
			InvoiceEditor.Row row = ((InvoiceEditor) editor).table().appendRow();
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
	
	private static SelectableVendor createVendor() {
		VendorEditor editor = ctx.newObject(VendorEditor.class);
		editor.setSwd("PDMVend");
		editor.commit();
		SelectableVendor vendor = editor.getId();
		return vendor;
	}
	

}
