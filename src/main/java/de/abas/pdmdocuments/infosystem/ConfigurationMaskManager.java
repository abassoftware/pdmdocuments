package de.abas.pdmdocuments.infosystem;

import org.apache.log4j.Logger;

import de.abas.erp.db.DbContext;
import de.abas.erp.db.infosystem.custom.owpdm.PdmDocuments;
import de.abas.pdmdocuments.infosystem.config.Configuration;
import de.abas.pdmdocuments.infosystem.config.ConfigurationHandler;
import de.abas.pdmdocuments.infosystem.utils.UtilwithAbasConnection;

public class ConfigurationMaskManager {

	protected static final Logger log = Logger.getLogger(ConfigurationMaskManager.class);

	private ConfigurationMaskManager() {
		throw new UnsupportedOperationException();
	}

	protected static void getConfigInMask(PdmDocuments head, DbContext ctx) {

		try {
			Configuration config = ConfigurationHandler.loadConfiguration();

			head.setYserver(config.getRestServer());
			head.setYuser(config.getRestUser());
			head.setYpassword(config.getRestPassword());
			head.setYtenant(config.getRestTenant());

			// Vorbelegung für SQL-Server falls noch nicht gespeichert

			head.setYsqlserver(checknull(config.getSqlServer()));
			head.setYsqlport(checknull(config.getSqlPort()));
			head.setYdatabase(checknull(config.getSqldatabase()));
			head.setYsqluser(checknull(config.getSqlUser()));
			head.setYsqlpassword(checknull(config.getSqlPassword()));
			head.setYsqldriver(checknull(config.getSqlDriver()));

			head.setYpdmsystem(config.getPdmSystem());

			head.setYbildschirmtypen(config.getFileTypesScreen());
			head.setYdrucktypen(config.getFileTypesPrinter());
			head.setYemailtypen(config.getFileTypesEmail());
			head.setYfieldfornumber(config.getPartFieldName());
			head.setYfieldforpartid(config.getPartProFileIDFieldName());
			head.setYfieldfororgname(config.getOrgNameFieldName());
			head.setYfieldfordocversid(config.getDocVersionBaseIDFieldName());
			head.setYfieldfordoctype(config.getDocTypeFieldName());
			head.setYdokart(config.getDokart());

		} catch (PdmDocumentsException e) {
			UtilwithAbasConnection.showErrorBox(ctx,
					UtilwithAbasConnection.getMessage("pdmDocument.error.loadKonfiguration") + "/n" + e.getMessage());
		}

	}

	protected static void saveconfig(DbContext ctx, PdmDocuments head, Configuration config) {
		try {
			config.setRestServer(head.getYserver(), head.getYuser(), head.getYpassword(), head.getYtenant());
			config.setSqlConnection(head.getYsqlserver(), head.getYsqlport(), head.getYdatabase(), head.getYsqluser(),
					head.getYsqlpassword(), head.getYsqldriver());
			config.setFiletypes(head.getYemailtypen(), head.getYdrucktypen(), head.getYbildschirmtypen());
			config.setPdmSystem(head.getYpdmsystem());
			config.setPartAbasNumberFieldName(head.getYfieldfornumber());
			config.setPartProFileIDFieldName(head.getYfieldforpartid());
			config.setDocVersionBaseIDFieldName(head.getYfieldfordocversid());
			config.setDocTypeFieldName(head.getYfieldfordoctype());
			config.setOrgNameFieldName(head.getYfieldfororgname());
			config.setDokart(head.getYdokart());
			ConfigurationHandler.saveConfigurationtoFile(config);
		} catch (PdmDocumentsException e) {
			log.error(e);
			UtilwithAbasConnection.showErrorBox(ctx,
					UtilwithAbasConnection.getMessage("main.saveconfiguration.error", e.getMessage()));

		}
	}

	private static int checknull(Integer value) {
		if (value != null) {
			return value;
		} else {
			return 0;
		}

	}

	private static String checknull(String value) {

		if (value != null) {
			return value;
		} else {
			return "";
		}

	}

}
