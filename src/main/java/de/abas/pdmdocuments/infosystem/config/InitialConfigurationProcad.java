package de.abas.pdmdocuments.infosystem.config;

import de.abas.erp.db.infosystem.custom.owpdm.PdmDocuments;

public class InitialConfigurationProcad {
	protected static final String SQL_DRIVER_DEFAULT = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	private InitialConfigurationProcad() {
		throw new UnsupportedOperationException();
	}

	public static void preFillProFileFields(PdmDocuments pdmDocuments) {

		if (checkFieldnameFieldsAreEmpty(pdmDocuments)) {
			pdmDocuments.setYfieldfornumber("/Part/pdmPartItemNumber");
			pdmDocuments.setYfieldforpartid("/Part/pdmPartID");
			pdmDocuments.setYfieldfororgname("/Document/orgName");
			pdmDocuments.setYfieldfordocversid("/Document/docVersionBaseId");
			pdmDocuments.setYfieldfordoctype("/Document/docType");
			pdmDocuments.setYsqldriver(SQL_DRIVER_DEFAULT);
		}
	}

	public static String getSqlDriverDefault() {
		return SQL_DRIVER_DEFAULT;
	}

	protected static boolean checkFieldnameFieldsAreEmpty(PdmDocuments head) {
		if (!head.getYfieldfornumber().isEmpty()) {
			return false;
		}
		if (!head.getYfieldforpartid().isEmpty()) {
			return false;
		}
		if (!head.getYfieldfororgname().isEmpty()) {
			return false;
		}
		if (!head.getYfieldfordocversid().isEmpty()) {
			return false;
		}
		if (!head.getYfieldfordoctype().isEmpty()) {
			return false;
		}
		if (!head.getYsqldriver().isEmpty()) {
			return false;
		}

		return true;
	}

}
