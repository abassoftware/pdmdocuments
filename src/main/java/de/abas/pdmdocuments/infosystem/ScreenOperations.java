package de.abas.pdmdocuments.infosystem;

import java.util.ArrayList;
import java.util.List;

import de.abas.erp.api.session.ERPInformation;
import de.abas.erp.api.session.OperatorInformation;
import de.abas.erp.common.type.enums.EnumPriorities;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.schema.company.Password;
import de.abas.erp.db.schema.userenums.UserEnumPdmSystems;
import de.abas.jfop.base.buffer.BufferFactory;
import de.abas.jfop.base.buffer.PrintBuffer;
import de.abas.pdmdocuments.infosystem.config.Configuration;

public class ScreenOperations {

	private static final String MASKKONTEXTFOP = "maskkontextfop";
	private static final String DEVELOPER = "DEVELOPER";
	private static final String KONFIGURATION = "KONFIGURATION";

	protected List<String> maskenkontext = new ArrayList<>();

	private void addMaskkontext(String konfiguration) {

		this.maskenkontext.add(konfiguration);
		actMaskContext();
	}

	private void removeMaskkontext(String konfiguration) {

		this.maskenkontext.remove(konfiguration);
		actMaskContext();
	}

	protected void replacePDMSystemInMaskkontext(UserEnumPdmSystems pdmSystem) {
		UserEnumPdmSystems[] list = UserEnumPdmSystems.values();
		for (UserEnumPdmSystems userEnumPdmSystems : list) {
			removeMaskkontext(userEnumPdmSystems.name().toUpperCase());
		}
		addMaskkontext(pdmSystem.name().toUpperCase());

	}

	private void actMaskContext() {
		BufferFactory buff = BufferFactory.newInstance();
		PrintBuffer printbuf = buff.getPrintBuffer();

		StringBuilder bld = new StringBuilder();
		for (String value : this.maskenkontext) {
			bld.append(" ");
			bld.append(value);
		}
		printbuf.assign(MASKKONTEXTFOP, bld.toString());
	}

	protected void showConfiguration(DbContext ctx, Configuration config) {

		checkScreenEnviroment(ctx);

		if (config.getPdmSystem() != null) {

			replacePDMSystemInMaskkontext(config.getPdmSystem());

		} else {

			UserEnumPdmSystems[] pdmSystemValues = UserEnumPdmSystems.values();
			for (UserEnumPdmSystems userEnumPdmSystems : pdmSystemValues) {

				if (userEnumPdmSystems != null) {

					addMaskkontext(userEnumPdmSystems.name().toUpperCase());

				}
			}
		}
	}

	private void checkScreenEnviroment(DbContext ctx) {

		OperatorInformation operatorInformation = new OperatorInformation(ctx);
		Password passw = operatorInformation.getPwdRecord();
		EnumPriorities prio = passw.getPrio();

		if (prio.compareTo(EnumPriorities.E) >= 0) {
			addMaskkontext(KONFIGURATION);
		}

		ERPInformation erpInfos = new ERPInformation(ctx);

		if (erpInfos.getInstNum() == 3) {
			addMaskkontext(DEVELOPER);
		}

	}

}
