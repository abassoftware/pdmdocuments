package de.abas.pdmdocuments.infosystem.webservices.coffee;

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
import org.mockito.runners.MockitoJUnitRunner;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.github.tomakehurst.wiremock.junit.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URL;
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

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import  com.github.tomakehurst.wiremock.*;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

class RestServiceCoffeeTest {
	
	static WireMockServer wmserver = new WireMockServer(options().dynamicPort());
	int port = wmserver.port();

	@BeforeAll
	static void startWireMockServer() {
		wmserver.start();
	}
	
	@AfterAll
	static void stopWireMockServer() {
		wmserver.stop();
	}
	

	 
	RestServiceCoffee restservicecoffeeWithTestServer;
	RestServiceCoffee restservicecoffeeWithMockConfig;
	
	@Test
	void testRestServiceCoffeeStringStringString() {
		restservicecoffeeWithTestServer = new RestServiceCoffee("localhost:" + port, "TestUser", "TestPasswort");
		assertNotNull(restservicecoffeeWithTestServer);
	}

	@Test
	void testRestServiceCoffeeConfiguration() {
		restservicecoffeeWithMockConfig = new RestServiceCoffee(mockConfiguration());
		assertNotNull(restservicecoffeeWithMockConfig);	
	}
	

	@Test
	void testGetAllDocuments() {

		List<PdmDocument> ergebnis = null;
		
		String[] fileTypList = new String[5];
		fileTypList[0] = "pdf";
		fileTypList[1] = "dxf";
		fileTypList[2] = "dwg";
		fileTypList[3] = "iges";
		fileTypList[4] = "step";

		restservicecoffeeWithTestServer = new RestServiceCoffee("localhost:" + port, "TestUser", "TestPasswort");

		
		wmserver.stubFor(get(urlEqualTo("/listing?json=%7B%22Extension%22%3A%22%22%2C%22Variables%22%3A%5B%7B%22Name%22%3A%22ABAS_Identnummer%22%2C%22Option%22%3A106%2C%22Value%22%3A%225004352%22%7D%5D%2C%22FileId%22%3A0%2C%22Version%22%3A0%7D")).willReturn(aResponse()
				.withStatus(200)
				.withHeader("Content-Type", "application/json")
				.withBody("[\n" + 
						"  {\n" + 
						"    \"FileID\": 21921,\n" + 
						"    \"Extension\": \"sldprt\",\n" + 
						"    \"FileName\": \"test2.sldprt\",\n" + 
						"    \"FilePath\": \"C:\\\\ROMA-PDM\\\\Konstruktion\\\\Auftrag\\\\2019\\\\Schnittstelle\\\\test2.sldprt\",\n" + 
						"    \"LocalVersion\": \"20\",\n" + 
						"    \"LatestVersion\": \"20\",\n" + 
						"    \"CheckedIn\": true,\n" + 
						"    \"CheckedInBy\": \"Admin\",\n" + 
						"    \"CheckedInDate\": \"08.10.2019 08:53:06\"\n" + 
						"  },\n" + 
						"  {\n" + 
						"    \"FileID\": 21923,\n" + 
						"    \"Extension\": \"SLDDRW\",\n" + 
						"    \"FileName\": \"test2.SLDDRW\",\n" + 
						"    \"FilePath\": \"C:\\\\ROMA-PDM\\\\Konstruktion\\\\Auftrag\\\\2019\\\\Schnittstelle\\\\test2.SLDDRW\",\n" + 
						"    \"LocalVersion\": \"19\",\n" + 
						"    \"LatestVersion\": \"19\",\n" + 
						"    \"CheckedIn\": true,\n" + 
						"    \"CheckedInBy\": \"Admin\",\n" + 
						"    \"CheckedInDate\": \"01.10.2019 11:19:34\"\n" + 
						"  },\n" + 
						"  {\n" + 
						"    \"FileID\": 21924,\n" + 
						"    \"Extension\": \"STEP\",\n" + 
						"    \"FileName\": \"test2.STEP\",\n" + 
						"    \"FilePath\": \"C:\\\\ROMA-PDM\\\\Konstruktion\\\\Auftrag\\\\2019\\\\Schnittstelle\\\\test2.STEP\",\n" + 
						"    \"LocalVersion\": \"3\",\n" + 
						"    \"LatestVersion\": \"3\",\n" + 
						"    \"CheckedIn\": true,\n" + 
						"    \"CheckedInBy\": \"Admin\",\n" + 
						"    \"CheckedInDate\": \"23.09.2019 09:26:18\"\n" + 
						"  }\n" + 
						"]")));
		
		wmserver.stubFor(get(urlEqualTo("/download?json=%7B%22FileId%22%3A21924%2C%22Version%22%3A+3%7D")).willReturn(aResponse()
                .withBodyFile("/test.pdf")));
			
		
		try {
			ergebnis = restservicecoffeeWithTestServer.getAllDocuments("5004352", fileTypList);
			
		} catch (PdmDocumentsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull(ergebnis);
		
	}
	
	
	@Test
	void testTestConnection() {
		wmserver.stubFor(get(urlEqualTo("/search")).willReturn(ok()));		
	
		restservicecoffeeWithTestServer = new RestServiceCoffee("localhost:" + port, "TestUser", "TestPasswort");
		boolean connection = restservicecoffeeWithTestServer.testConnection();
		assertEquals(true, connection);
	}
	


	private Configuration mockConfiguration() {
		Configuration configuration = mock(Configuration.class);
		//when(configuration.getFileTypesEmail()).thenReturn(2)
		return configuration;
	}
	
}
