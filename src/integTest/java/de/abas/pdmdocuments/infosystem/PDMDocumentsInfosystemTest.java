package de.abas.pdmdocuments.infosystem;

import java.util.List;

import de.abas.erp.db.SelectableObject;
import de.abas.erp.db.infosystem.custom.owpdm.PdmDocuments;
import de.abas.esdk.test.util.EsdkIntegTest;
import de.abas.pdmdocuments.infosystem.CreatePurchasingTestData;
import de.abas.pdmdocuments.infosystem.CreateSalesTestData;



public class PDMDocumentsInfosystemTest extends EsdkIntegTest {

	public PdmDocuments infosystem = ctx.openInfosystem(PdmDocuments.class);
	
	CreateSalesTestData salesData = new CreateSalesTestData();
	CreatePurchasingTestData purchsasingData = new CreatePurchasingTestData();
	
	List<SelectableObject> salesObject = salesData.prepare();
	List<SelectableObject> purchasingObject = purchsasingData.prepare();
	

	
	
}
