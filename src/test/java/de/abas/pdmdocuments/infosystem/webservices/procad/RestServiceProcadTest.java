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
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.containers.MySQLContainer;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.github.tomakehurst.wiremock.junit.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.print.Printable;
import java.io.Console;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
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
	
	//static WireMockServer sqlserver = new WireMockServer(options().port(6666).basicAdminAuthenticator("root", "password"));


    static  MSSQLServerContainer mssqlserver = new MSSQLServerContainer();	
	
    static int sqlport;
	

	@BeforeAll
	static void startWireMockServer() {
		wmserver.start();
		mssqlserver.start();	
		
		String URL = mssqlserver.getJdbcUrl();
		String[] url = URL.split(":");
		sqlport = Integer.valueOf(url[3].toString());
		
		
		createDatabase(URL);
		createTable(URL);		
		insertRecordintoTable(URL);
		
		createRestApi();

	}

	
	@AfterAll
	static void stopWireMockServer() {
		wmserver.stop();
		mssqlserver.stop();
	}
	
	
	RestServiceProcad restServiceProcadWithMockConfig;


	@Test
	void testRestServiceProcad() {
		
		restServiceProcadWithMockConfig = new RestServiceProcad(mockConfiguration());
		assertNotNull(restServiceProcadWithMockConfig);	
	}

	@Test
	void testSearchPdmProductID() {		
		
		
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
	void testGetAllDocumentsStatus200() {
		String[] fileTypList = new String[4];
		fileTypList[0] = "pdf";
		fileTypList[1] = "dxf";
		fileTypList[2] = "dwfx";
		fileTypList[3] = "dwf";
		
		
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
	void testGetAllDocumentsStatus404() {

		String[] fileTypList = new String[4];
		fileTypList[0] = "pdf";
		fileTypList[1] = "dxf";
		fileTypList[2] = "dwfx";
		fileTypList[3] = "dwf";
		
	
		restServiceProcadWithMockConfig = new RestServiceProcad(mockConfiguration());
		List<PdmDocument> ergebnis = null;
		
		try {
			 ergebnis = restServiceProcadWithMockConfig.getAllDocuments("50428", fileTypList);
					
		} catch (PdmDocumentsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull(ergebnis);	

	}

	@Test
	void testTestRestService() {
			
		restServiceProcadWithMockConfig = new RestServiceProcad(mockConfiguration());
		Boolean ergebnis = false;
		try {
			ergebnis = restServiceProcadWithMockConfig.testConnection();
		} catch (PdmDocumentsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(true, ergebnis);
		
	}

	static void createRestApi() {
		
		String json = "{\n" + 
				"   \"Values\":{\n" + 
				"      \"/Document/pdmDocID\":10008,\n" + 
				"      \"/Document/docType\":\"/Document/Archiv-Zeichnung/\",\n" + 
				"      \"/Document/pdmDocFileSystemPath\":49,\n" + 
				"      \"/Document/fileSize\":610988,\n" + 
				"      \"/Document/orgName\":\"Deckel.pdf\",\n" + 
				"      \"/Document/pdmDocFileExtension\":\"pdf\",\n" + 
				"      \"/Document/pdmDocMetaCreateDate\":\"2018-03-13T15:20:56\",\n" + 
				"      \"/Document/pdmDocMetaCreateUser\":\"procad\",\n" + 
				"      \"/Document/pdmDocMetaModifyDate\":\"0001-01-01T00:00:00\",\n" + 
				"      \"/Document/pdmDocMetaModifyUser\":\"\",\n" + 
				"      \"/Document/docCreateDate\":\"2018-03-13T15:22:39\",\n" + 
				"      \"/Document/pdmDocFileCreateUser\":\"procad\",\n" + 
				"      \"/Document/docChangeDate\":\"0001-01-01T00:00:00\",\n" + 
				"      \"/Document/pdmDocFileModifyUser\":\"\",\n" + 
				"      \"/Document/pdmDocSourceSystemID\":-1,\n" + 
				"      \"/Document/pdmDocDesignType\":0,\n" + 
				"      \"/Document/pdmDocCADType\":-1,\n" + 
				"      \"/Document/docRevision\":1,\n" + 
				"      \"/Document/docVersion\":1,\n" + 
				"      \"/Document/docVersionBaseId\":10008,\n" + 
				"      \"/Document/pdmDocDate\":\"2018-03-13T10:59:59\",\n" + 
				"      \"/Document/pdmDocTitle\":\"Deckel Zeichnung\",\n" + 
				"      \"/Document/pdmDocProjectNumber\":\"\",\n" + 
				"      \"/Document/pdmDocProjectName\":\"\",\n" + 
				"      \"/Document/pdmDocUserLockDate\":\"0001-01-01T00:00:00\",\n" + 
				"      \"/Document/pdmDocUserLockUser\":\"\",\n" + 
				"      \"/Document/pdmDocStateName\":200000,\n" + 
				"      \"/Document/pdmDocStateModifyDate\":\"2019-11-25T07:29:33\",\n" + 
				"      \"/Document/pdmDocStateModifyUser\":\"procad\",\n" + 
				"      \"/Document/pdmDocRevisionChainVersion\":1,\n" + 
				"      \"/Document/pdmDocRevisionChainIndex\":1,\n" + 
				"      \"/Document/pdmDocGUID\":\"471e6e25-26f8-47f9-ba5a-586e04f4e6c6\",\n" + 
				"      \"/Document/pdmDocERPStateID\":20101,\n" + 
				"      \"/Document/pdmDocERPErrorMessage\":20101,\n" + 
				"      \"/Document/pdmDocERPTopStateID\":20101,\n" + 
				"      \"/Document/pdmDocERPTopErrorMessage\":20101,\n" + 
				"      \"/Document/pdmDocFirstIssueDate\":\"0001-01-01T00:00:00\",\n" + 
				"      \"/Document/pdmDocCheckDate\":\"0001-01-01T00:00:00\",\n" + 
				"      \"/Document/pdmDocApproveDate\":\"0001-01-01T00:00:00\",\n" + 
				"      \"/Document/pdmDocNumber\":\"\",\n" + 
				"      \"/Document/pdmDocFirstIssueUser\":\"\",\n" + 
				"      \"/Document/pdmDocCheckUser\":\"\",\n" + 
				"      \"/Document/pdmDocApproveUser\":\"\",\n" + 
				"      \"/Document/pdmDocSourceSystemID[v]\":\"No CAD-System\",\n" + 
				"      \"/Document/pdmDocFileSystemPath[v]\":\"<@VM-AMSW2012R2@>E:\\\\PRODATA\\\\archzei\\\\data0000\",\n" + 
				"      \"/Document/pdmDocStateName[v]\":\"Dokument in Freigabe\",\n" + 
				"      \"/Document/docRevision[v]\":\"\",\n" + 
				"      \"/Document/docVersion[v]\":\"\",\n" + 
				"      \"/Document/pdmDocERPErrorMessage[v]\":\"\",\n" + 
				"      \"/Document/pdmDocERPTopErrorMessage[v]\":\"\",\n" + 
				"      \"/Document/Archiv-Zeichnung/pdmDocDrawingPage\":0,\n" + 
				"      \"/Document/Archiv-Zeichnung/pdmDocDrawingPages\":0,\n" + 
				"      \"/Document/Archiv-Zeichnung/pdmDocDrawingIssueDate\":\"0001-01-01T00:00:00\",\n" + 
				"      \"/Document/Archiv-Zeichnung/pdmDocDrawingNumber\":\"0815\",\n" + 
				"      \"/Document/Archiv-Zeichnung/pdmDocDrawingScale\":\"\",\n" + 
				"      \"/Document/Archiv-Zeichnung/pdmDocDrawingSize\":\"\",\n" + 
				"      \"/Document/Archiv-Zeichnung/pdmDocDrawingFirstIssuerName\":\"\",\n" + 
				"      \"/Document/Archiv-Zeichnung/pdmDocSubTypeName\":\"\",\n" + 
				"      \"/Document/Archiv-Zeichnung/pdmDocDescription\":\"\"\n" + 
				"   },\n" + 
				"   \"Header\":{\n" + 
				"      \"Links\":[\n" + 
				"         {\n" + 
				"            \"Href\":\"http://192.168.6.28/procad/profile/Api/Profile/objects/Document/10008\",\n" + 
				"            \"Type\":\"self\"\n" + 
				"         },\n" + 
				"         {\n" + 
				"            \"Href\":\"http://192.168.6.28/procad/profile/Api/Profile/objects/Document/10008/file\",\n" + 
				"            \"Type\":\"file\"\n" + 
				"         }\n" + 
				"      ],\n" + 
				"      \"HasFile\":true\n" + 
				"   }\n" + 
				"}\n";
		
		wmserver.stubFor(get(urlEqualTo("/procad/profile/api/profile86/objects/Part?query='/Part/pdmPartItemNumber'='50426'")).willReturn(aResponse()
				.withStatus(200)
				.withHeader("Content-Type", "application/json")
				.withBody("{\"Objects\":[{\"Values\":{\"/Part/pdmPartID\":46933,\"/Part/pdmPartProductGroup\":\"XX1570\",\"/Part/pdmPartCreateUser\":\"BizTalkImport\",\"/Part/pdmPartCreateDate\":\"2017-08-02T02:37:48\",\"/Part/pdmPartLockUser\":\"\",\"/Part/pdmPartLockDate\":\"0001-01-01T00:00:00\",\"/Part/pdmPartState\":300,\"/Part/pdmPartModifyDate\":\"2019-11-18T08:51:47\",\"/Part/pdmPartModifyUser\":\"BizTalkImport\",\"/Part/pdmPartBOMUnit\":\"Stück\",\"/Part/DEL_ pdmPartBasicMaterialName\":\"\",\"/Part/pdmPartDimensions\":\"\",\"/Part/pdmPartUsage\":\"Fremdbeschaffung\",\"/Part/pdmPartItemNumber\":\"50426\",\"/Part/pdmPartStateModifyUser\":\"\",\"/Part/pdmPartStateModifyDate\":\"0001-01-01T00:00:00\",\"/Part/pdmPartBasicMaterialName\":\"\",\"/Part/pdmPartRemarkLong\":\"\",\"/Part/pdmPartRevision\":1,\"/Part/pdmPartVersion\":1,\"/Part/pdmPartRevisionChainAncestor\":46933,\"/Part/pdmPartRevisionChainVersion\":1,\"/Part/pdmPartRevisionChainIndex\":1,\"/Part/pdmPartGUID\":\"3110db48-43e2-4a59-84e8-4717c6110a13\",\"/Part/pdmPartERPStateID\":100,\"/Part/pdmPartERPErrorMessage\":20102,\"/Part/pdmPartERPTopStateID\":20101,\"/Part/pdmPartERPTopErrorMessage\":0,\"/Part/pdmPartState[v]\":\"Freigegeben Teil\",\"/Part/pdmPartRevision[v]\":\"\",\"/Part/pdmPartVersion[v]\":\"\",\"/Part/pdmPartERPErrorMessage[v]\":\"\",\"/Part/pdmPartERPTopErrorMessage[v]\":\"\"},\"Header\":{\"Links\":[{\"Href\":\"http://192.168.99.89/procad/profile/Api/profile86/objects/Part/46933\",\"Type\":\"self\"}]}}]}")));
		
		
		wmserver.stubFor(get(urlEqualTo("/procad/profile/api/profile86/objects/Document/116043/file")).willReturn(aResponse()
				.withStatus(200)
				.withHeader("Content-Type", "application/json")
				.withBody(json)));
		
		wmserver.stubFor(get(urlEqualTo("/procad/profile/api/profile86/objects/Document/116043/")).willReturn(aResponse()
				.withStatus(200)
				.withHeader("Content-Type", "application/json")
				.withBody(json)));
		
		wmserver.stubFor(get(urlEqualTo("/procad/profile/api/profile86/objects/Part?query='/Part/pdmPartItemNumber'='50428'")).willReturn(aResponse()
				.withStatus(200)
				.withHeader("Content-Type", "application/json")
				.withBody("{\"Objects\":[{\"Values\":{\"/Part/pdmPartID\":46900,\"/Part/pdmPartProductGroup\":\"XX1570\",\"/Part/pdmPartCreateUser\":\"BizTalkImport\",\"/Part/pdmPartCreateDate\":\"2017-08-02T02:37:48\",\"/Part/pdmPartLockUser\":\"\",\"/Part/pdmPartLockDate\":\"0001-01-01T00:00:00\",\"/Part/pdmPartState\":300,\"/Part/pdmPartModifyDate\":\"2019-11-18T08:51:47\",\"/Part/pdmPartModifyUser\":\"BizTalkImport\",\"/Part/pdmPartBOMUnit\":\"Stück\",\"/Part/DEL_ pdmPartBasicMaterialName\":\"\",\"/Part/pdmPartDimensions\":\"\",\"/Part/pdmPartUsage\":\"Fremdbeschaffung\",\"/Part/pdmPartItemNumber\":\"50426\",\"/Part/pdmPartStateModifyUser\":\"\",\"/Part/pdmPartStateModifyDate\":\"0001-01-01T00:00:00\",\"/Part/pdmPartBasicMaterialName\":\"\",\"/Part/pdmPartRemarkLong\":\"\",\"/Part/pdmPartRevision\":1,\"/Part/pdmPartVersion\":1,\"/Part/pdmPartRevisionChainAncestor\":46933,\"/Part/pdmPartRevisionChainVersion\":1,\"/Part/pdmPartRevisionChainIndex\":1,\"/Part/pdmPartGUID\":\"3110db48-43e2-4a59-84e8-4717c6110a13\",\"/Part/pdmPartERPStateID\":100,\"/Part/pdmPartERPErrorMessage\":20102,\"/Part/pdmPartERPTopStateID\":20101,\"/Part/pdmPartERPTopErrorMessage\":0,\"/Part/pdmPartState[v]\":\"Freigegeben Teil\",\"/Part/pdmPartRevision[v]\":\"\",\"/Part/pdmPartVersion[v]\":\"\",\"/Part/pdmPartERPErrorMessage[v]\":\"\",\"/Part/pdmPartERPTopErrorMessage[v]\":\"\"},\"Header\":{\"Links\":[{\"Href\":\"http://192.168.99.89/procad/profile/Api/profile86/objects/Part/46933\",\"Type\":\"self\"}]}}]}")));
		
		
		wmserver.stubFor(get(urlEqualTo("/procad/profile/api/profile86/objects/Document/176839/")).willReturn(aResponse()
				.withStatus(404)));
		
		wmserver.stubFor(get(urlEqualTo("/procad/profile/api/profile86/objects/Part?query='/Part/pdmPartItemNumber'='116043'")).willReturn(aResponse()
				.withStatus(200)
				.withHeader("Content-Type", "application/json")
				.withBody("{\"Objects\":[{\"Values\":{\"/Part/pdmPartID\":176839,\"/Part/pdmPartProductGroup\":\"SA0001\",\"/Part/pdmPartCreateUser\":\"BizTalkImport\",\"/Part/pdmPartCreateDate\":\"2019-01-17T13:08:39\",\"/Part/pdmPartLockUser\":\"\",\"/Part/pdmPartLockDate\":\"0001-01-01T00:00:00\",\"/Part/pdmPartState\":100,\"/Part/pdmPartModifyDate\":\"2019-10-11T13:35:41\",\"/Part/pdmPartModifyUser\":\"BizTalkImport\",\"/Part/pdmPartBOMUnit\":\"Stück\",\"/Part/DEL_ pdmPartBasicMaterialName\":\"\",\"/Part/pdmPartDimensions\":\"\",\"/Part/pdmPartUsage\":\"Eigenfertigung\",\"/Part/pdmPartItemNumber\":\"116043\",\"/Part/pdmPartStateModifyUser\":\"\",\"/Part/pdmPartStateModifyDate\":\"0001-01-01T00:00:00\",\"/Part/pdmPartBasicMaterialName\":\"\",\"/Part/pdmPartRemarkLong\":\"\",\"/Part/pdmPartRevision\":1,\"/Part/pdmPartVersion\":1,\"/Part/pdmPartRevisionChainAncestor\":176839,\"/Part/pdmPartRevisionChainVersion\":1,\"/Part/pdmPartRevisionChainIndex\":1,\"/Part/pdmPartGUID\":\"71fe00f0-8125-4dfa-b90b-7ca23e0795d5\",\"/Part/pdmPartERPStateID\":100,\"/Part/pdmPartERPErrorMessage\":20102,\"/Part/pdmPartERPTopStateID\":20101,\"/Part/pdmPartERPTopErrorMessage\":0,\"/Part/pdmPartState[v]\":\"In Arbeit Teil\",\"/Part/pdmPartRevision[v]\":\"\",\"/Part/pdmPartVersion[v]\":\"\",\"/Part/pdmPartERPErrorMessage[v]\":\"\",\"/Part/pdmPartERPTopErrorMessage[v]\":\"\"},\"Header\":{\"Links\":[{\"Href\":\"http://192.168.99.89/procad/profile/Api/profile86/objects/Part/176839\",\"Type\":\"self\"}]}}]}\n" + 
						"")));
		wmserver.stubFor(get("/procad/profile/api/version")
			    .willReturn(ok()));	
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
		when(configuration.getSqlPort()).thenReturn(sqlport);	
		when(configuration.getSqldatabase()).thenReturn("dbtest");	
		when(configuration.getSqlUser()).thenReturn("SA");	
		when(configuration.getSqlPassword()).thenReturn("A_Str0ng_Required_Password");	
		when(configuration.getSqlDriver()).thenReturn("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		
		return configuration;
	}
	
	private static void insertRecordintoTable(String URL) {
		Connection conn = null;
		   Statement stmt = null;
		   try{
			  Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		      conn = DriverManager.getConnection(URL + ";databaseName=dbtest", "SA", "A_Str0ng_Required_Password");

		      stmt = conn.createStatement();
		      String sql = "INSERT INTO VERBIND " +
		                   "VALUES (46933, 116043, 2, 3)";
		      stmt.executeUpdate(sql);
		      sql = "INSERT INTO VERBIND " +
	                   "VALUES (46900, 176839, 2, 3)";
	      stmt.executeUpdate(sql);
		      System.out.println("Inserted records into the table...");

		   }catch(SQLException se){
		      se.printStackTrace();
		   }catch(Exception e){
		      e.printStackTrace();
		   }finally{
		      try{
		         if(stmt!=null)
		            conn.close();
		      }catch(SQLException se){
		      }
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }
	}

	private static void createTable(String URL) {
		Connection conn = null;
		   Statement stmt = null;
		   try{
		      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		      conn = DriverManager.getConnection(URL + ";databaseName=dbtest", "SA", "A_Str0ng_Required_Password");
		      
		      stmt = conn.createStatement();
		      
		      String sql = "CREATE TABLE VERBIND " +
		                   "(vb_objidnr1 INTEGER, " +
		                   " vb_objidnr2 INTEGER, " + 
		                   " vb_objtyp1 INTEGER," + 
		                   " vb_objtyp2 INTEGER)"; 

		      stmt.executeUpdate(sql);
		   }catch(SQLException se){
		      se.printStackTrace();
		   }catch(Exception e){
		      e.printStackTrace();
		   }finally{
		      try{
		         if(stmt!=null)
		            conn.close();
		      }catch(SQLException se){
		      }
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }
	}

	private static void createDatabase(String URL) {
		Connection conn = null;
		Statement stmt = null;

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(
					URL, "SA", "A_Str0ng_Required_Password");
			
		      stmt = conn.createStatement();
		      
		      String sql = "CREATE DATABASE dbtest";
		      stmt.executeUpdate(sql);
			
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
		      try{
		         if(stmt!=null)
		            stmt.close();
		      }catch(SQLException se2){
		      }
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }
	}
}
