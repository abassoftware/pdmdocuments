package de.abas.pdmdocuments.infosystem.webservices.coffee;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.abas.pdmdocuments.infosystem.config.Configuration;




class RestServiceCoffeeTest {

	@Test
	void testRestServiceCoffeeStringStringString() {
		fail("Not yet implemented");
	}

	@Test
	void testRestServiceCoffeeConfiguration() {
		RestServiceCoffee restservicecoffee = new RestServiceCoffee(mockConfiguration());
		assertNotNull(restservicecoffee);	
	}

	private Configuration mockConfiguration() {
		Configuration configuration = mock(Configuration.class);
		//when(configuration.getFileTypesEmail()).thenReturn(2)
		return configuration;
	}

	@Test
	void testGetAllDocuments() {
		fail("Not yet implemented");
	}

	@Test
	void testTestConnection() {
		fail("Not yet implemented");
	}

	
}
