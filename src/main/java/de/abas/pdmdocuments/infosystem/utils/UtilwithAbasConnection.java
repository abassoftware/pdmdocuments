package de.abas.pdmdocuments.infosystem.utils;

import java.util.Locale;

import de.abas.eks.jfop.remote.EKS;
import de.abas.erp.api.gui.TextBox;
import de.abas.erp.axi.screen.ScreenControl;
import de.abas.erp.db.DbContext;

public class UtilwithAbasConnection {

	private UtilwithAbasConnection() {
		throw new IllegalStateException("Utility class");
	}

	private static Locale getLocale() {
		Locale locale = Locale.ENGLISH;
		try {
			return EKS.getFOPSessionContext().getOperatingLangLocale();
		} catch (final NullPointerException e) {
			return locale;
		}
	}

	public static void showErrorBox(DbContext ctx, String message) {
		new TextBox(ctx, Util.getMessage("main.exception.title", getLocale()), message).show();
	}

	public static String getMessage(String key) {
		return Util.getMessage(key, getLocale());
	}

	public static String getMessage(String key, Object... params) {

		return Util.getMessage(key, getLocale(), params);
	}

	public static void shownoticebar(DbContext ctx, ScreenControl screenControl, String message) {

		screenControl.setNote(Util.getMessage("main.exception.title", getLocale()) + " " + message);
		screenControl.setNote(Util.getMessage("main.exception.title", getLocale()) + " " + message, true, false);
	}
}
