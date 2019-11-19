package de.abas.pdmdocuments.infosystem.webservices.procad;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.client.WireMockBuilder;

import de.abas.pdmdocuments.infosystem.PdmDocumentsException;
import de.abas.pdmdocuments.infosystem.config.Configuration;
import de.abas.pdmdocuments.infosystem.webservices.procad.*;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import org.jboss.resteasy.client.jaxrs.BasicAuthentication;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.github.tomakehurst.wiremock.junit.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.sql.DriverManager;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.abas.erp.db.schema.userenums.UserEnumPdmSystems;
import de.abas.pdmdocuments.infosystem.DocumentSearchfactory;
import de.abas.pdmdocuments.infosystem.PdmDocumentsException;
import de.abas.pdmdocuments.infosystem.config.Configuration;
import de.abas.pdmdocuments.infosystem.data.PdmDocument;
import de.abas.pdmdocuments.infosystem.utils.UtilwithAbasConnection;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import  com.github.tomakehurst.wiremock.*;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

class RestServiceProcadTest {
	
	static WireMockServer wmserver = new WireMockServer(options().dynamicPort());
	int port = wmserver.port();
	
	static WireMockServer sqlserver = new WireMockServer(options().port(6666).basicAdminAuthenticator("root", "password"));

	@BeforeAll
	static void startWireMockServer() {
		wmserver.start();
		sqlserver.start();
		    
	}
	
	@AfterAll
	static void stopWireMockServer() {
		wmserver.stop();
		sqlserver.stop();
	}
	
	
	RestServiceProcad restServiceProcadWithMockConfig;


	@Test
	void testRestServiceProcad() {
		wmserver.stubFor(get("/procad/profile/api/version")
			    .willReturn(ok()));	
		
		restServiceProcadWithMockConfig = new RestServiceProcad(mockConfiguration());
		assertNotNull(restServiceProcadWithMockConfig);	
	}

	@Test
	void testSearchPdmProductID() {		
		
		wmserver.stubFor(get(urlEqualTo("/procad/profile/api/profile86/objects/Part?query='/Part/pdmPartItemNumber'='116043'")).willReturn(aResponse()
				.withStatus(200)
				.withHeader("Content-Type", "application/json")
				.withBody("{\"Objects\":[{\"Values\":{\"/Part/pdmPartID\":176839,\"/Part/pdmPartProductGroup\":\"SA0001\",\"/Part/pdmPartCreateUser\":\"BizTalkImport\",\"/Part/pdmPartCreateDate\":\"2019-01-17T13:08:39\",\"/Part/pdmPartLockUser\":\"\",\"/Part/pdmPartLockDate\":\"0001-01-01T00:00:00\",\"/Part/pdmPartState\":100,\"/Part/pdmPartModifyDate\":\"2019-10-11T13:35:41\",\"/Part/pdmPartModifyUser\":\"BizTalkImport\",\"/Part/pdmPartBOMUnit\":\"Stück\",\"/Part/DEL_ pdmPartBasicMaterialName\":\"\",\"/Part/pdmPartDimensions\":\"\",\"/Part/pdmPartUsage\":\"Eigenfertigung\",\"/Part/pdmPartItemNumber\":\"116043\",\"/Part/pdmPartStateModifyUser\":\"\",\"/Part/pdmPartStateModifyDate\":\"0001-01-01T00:00:00\",\"/Part/pdmPartBasicMaterialName\":\"\",\"/Part/pdmPartRemarkLong\":\"\",\"/Part/pdmPartRevision\":1,\"/Part/pdmPartVersion\":1,\"/Part/pdmPartRevisionChainAncestor\":176839,\"/Part/pdmPartRevisionChainVersion\":1,\"/Part/pdmPartRevisionChainIndex\":1,\"/Part/pdmPartGUID\":\"71fe00f0-8125-4dfa-b90b-7ca23e0795d5\",\"/Part/pdmPartERPStateID\":100,\"/Part/pdmPartERPErrorMessage\":20102,\"/Part/pdmPartERPTopStateID\":20101,\"/Part/pdmPartERPTopErrorMessage\":0,\"/Part/pdmPartState[v]\":\"In Arbeit Teil\",\"/Part/pdmPartRevision[v]\":\"\",\"/Part/pdmPartVersion[v]\":\"\",\"/Part/pdmPartERPErrorMessage[v]\":\"\",\"/Part/pdmPartERPTopErrorMessage[v]\":\"\"},\"Header\":{\"Links\":[{\"Href\":\"http://192.168.99.89/procad/profile/Api/profile86/objects/Part/176839\",\"Type\":\"self\"}]}}]}\n" + 
						"")));
		
		restServiceProcadWithMockConfig = new RestServiceProcad(mockConfiguration());
		String ergebnis = null;
		
		try {
			 ergebnis = restServiceProcadWithMockConfig.searchPdmProductID("116043");
					
		} catch (PdmDocumentsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull(ergebnis);
	}

	@Test
	void testGetAllDocuments() {
		String[] fileTypList = new String[4];
		fileTypList[0] = "pdf";
		fileTypList[1] = "dxf";
		fileTypList[2] = "dwfx";
		fileTypList[3] = "dwf";
		
		wmserver.stubFor(get(urlEqualTo("/procad/profile/api/profile86/objects/Part?query='/Part/pdmPartItemNumber'='50426'")).willReturn(aResponse()
				.withStatus(200)
				.withHeader("Content-Type", "application/json")
				.withBody("{\"Objects\":[{\"Values\":{\"/Part/pdmPartID\":46933,\"/Part/pdmPartProductGroup\":\"XX1570\",\"/Part/pdmPartCreateUser\":\"BizTalkImport\",\"/Part/pdmPartCreateDate\":\"2017-08-02T02:37:48\",\"/Part/pdmPartLockUser\":\"\",\"/Part/pdmPartLockDate\":\"0001-01-01T00:00:00\",\"/Part/pdmPartState\":300,\"/Part/pdmPartModifyDate\":\"2019-11-18T08:51:47\",\"/Part/pdmPartModifyUser\":\"BizTalkImport\",\"/Part/pdmPartBOMUnit\":\"Stück\",\"/Part/DEL_ pdmPartBasicMaterialName\":\"\",\"/Part/pdmPartDimensions\":\"\",\"/Part/pdmPartUsage\":\"Fremdbeschaffung\",\"/Part/pdmPartItemNumber\":\"50426\",\"/Part/pdmPartStateModifyUser\":\"\",\"/Part/pdmPartStateModifyDate\":\"0001-01-01T00:00:00\",\"/Part/pdmPartBasicMaterialName\":\"\",\"/Part/pdmPartRemarkLong\":\"\",\"/Part/pdmPartRevision\":1,\"/Part/pdmPartVersion\":1,\"/Part/pdmPartRevisionChainAncestor\":46933,\"/Part/pdmPartRevisionChainVersion\":1,\"/Part/pdmPartRevisionChainIndex\":1,\"/Part/pdmPartGUID\":\"3110db48-43e2-4a59-84e8-4717c6110a13\",\"/Part/pdmPartERPStateID\":100,\"/Part/pdmPartERPErrorMessage\":20102,\"/Part/pdmPartERPTopStateID\":20101,\"/Part/pdmPartERPTopErrorMessage\":0,\"/Part/pdmPartState[v]\":\"Freigegeben Teil\",\"/Part/pdmPartRevision[v]\":\"\",\"/Part/pdmPartVersion[v]\":\"\",\"/Part/pdmPartERPErrorMessage[v]\":\"\",\"/Part/pdmPartERPTopErrorMessage[v]\":\"\"},\"Header\":{\"Links\":[{\"Href\":\"http://192.168.99.89/procad/profile/Api/profile86/objects/Part/46933\",\"Type\":\"self\"}]}}]}")));
		
		restServiceProcadWithMockConfig = new RestServiceProcad(mockConfiguration());
		List<PdmDocument> ergebnis = null;
		
		try {
			 ergebnis = restServiceProcadWithMockConfig.getAllDocuments("50426", fileTypList);
					
		} catch (PdmDocumentsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull(ergebnis);	
	}

	@Test
	void testTestRestService() {
		
		
		sqlserver.stubFor(get(urlEqualTo(";databasename=dbtest")).willReturn(aResponse()
				.withStatus(200)
				.withHeader("Content-Type", "application/json")
				.withHeader("User", "root")
				.withHeader("Password", "password")
				.withBody("{\"ConnectionID\":\"1\", \"ClientConnectionId\": \"8d66a725-708d-4366-8925-9a175b393962\"}")));
		
		restServiceProcadWithMockConfig = new RestServiceProcad(mockConfiguration());
		Boolean ergebnis = false;
		try {
			ergebnis = restServiceProcadWithMockConfig.testConnection();
		} catch (PdmDocumentsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(true, ergebnis);
		
		/*
		this.connection = DriverManager.getConnection(
				"jdbc:sqlserver://" + this.server + ":" + this.port + ";databasename=" + this.database,
				this.user, this.password);
		
		private void openConnection() throws PdmDocumentsException {
		try {
			if (this.connection.isClosed()) {
				Class.forName(this.driver);
				this.connection = DriverManager.getConnection(
						"jdbc:sqlserver://" + this.server + ":" + this.port + ";databasename=" + this.database,
						this.user, this.password);
				log.info(UtilwithAbasConnection.getMessage("pdmDocument.info.sqlconnection.connect"));
			}

		} catch (Exception e) {
			log.error(UtilwithAbasConnection.getMessage(PDM_DOCUMENT_ERROR_SQLCONNECTION_CONNECT), e);
			throw new PdmDocumentsException(UtilwithAbasConnection.getMessage(PDM_DOCUMENT_ERROR_SQLCONNECTION_CONNECT),
					e);
		}
	}
		
				*/
	}

	@Test
	void testTestConnection() {
		fail("Not yet implemented");
	}

	private Configuration mockConfiguration() {
		Configuration configuration = mock(Configuration.class);
		when(configuration.getRestServer()).thenReturn("localhost:" + port);
		when(configuration.getRestUser()).thenReturn("procad");
		when(configuration.getRestPassword()).thenReturn("procad");
		when(configuration.getRestTenant()).thenReturn("profile86");	
		when(configuration.getPartFieldName()).thenReturn("/Part/pdmPartItemNumber");	
		when(configuration.getPartProFileIDFieldName()).thenReturn("/Part/pdmPartID");	

		when(configuration.getSqlServer()).thenReturn("localhost");	
		when(configuration.getSqlPort()).thenReturn(port);	
		when(configuration.getSqldatabase()).thenReturn("dbtest");	
		when(configuration.getSqlUser()).thenReturn("root");	
		when(configuration.getSqlPassword()).thenReturn("password");	
		when(configuration.getSqlDriver()).thenReturn("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		
		return configuration;
	}
}
