package de.abas.pdmdocuments.coffee.infosystem;

import org.apache.log4j.Logger;

import de.abas.pdmdocuments.coffee.infosystem.config.Configuration;
import de.abas.pdmdocuments.coffee.infosystem.webservices.keytech.RestServiceKeytech;
import de.abas.pdmdocuments.coffee.infosystem.webservices.procad.RestServiceProcad;

public class DocumentSearchfactory {

	protected final static Logger log = Logger.getLogger(DocumentSearchfactory.class);

	public DocumentsInterface create(Configuration config) throws PdmDocumentsException {

		// testserver(server);

		switch (config.getPdmSystem()) {
		case PROFILE: {
			log.info("Procad Service");
			DocumentsInterface restService = new RestServiceProcad(config);
			if (restService.testConnection()) {
				return restService;
			}

		}
			break;
		case KEYTECH: {
			log.info("Keytech Service");
			DocumentsInterface restService = new RestServiceKeytech(config);
			if (restService.testConnection()) {
				return restService;
			}

		}
			break;
		default:

			break;
		}

		return null;

	}

}
