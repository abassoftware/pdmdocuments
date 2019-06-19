package de.abas.pdmdocuments.coffee.infosystem.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

import de.abas.eks.jfop.remote.EKS;
import de.abas.erp.api.gui.TextBox;
import de.abas.erp.db.DbContext;

public class Util {

	private final static String MESSAGE_BASE = "de.abasgmbh.pdmDocuments.infosystem.messages";
	private static String[][] UMLAUT_REPLACEMENTS = { { new String("Ä"), "Ae" }, { new String("Ü"), "Ue" },
			{ new String("Ö"), "Oe" }, { new String("ä"), "ae" }, { new String("ü"), "ue" }, { new String("ö"), "oe" },
			{ new String("ß"), "ss" } };
	private static String[][] SONDERZEICHEN_REPLACEMENTS = { { new String("/"), "_" }, { new String(" "), "_" },
			{ new String(";"), "_" }, { new String("\\"), "_" }, { new String("="), "_" } };
	private static Locale locale = Locale.ENGLISH;

	private static Locale getLocale() {
		try {
			return EKS.getFOPSessionContext().getOperatingLangLocale();
		} catch (final NullPointerException e) {
			return locale;
		}
	}

	public static String getMessage(String key) {
		final ResourceBundle rb = ResourceBundle.getBundle(MESSAGE_BASE, getLocale());
		return rb.getString(key);
	}

	public static String getMessage(String key, Object... params) {
		final ResourceBundle rb = ResourceBundle.getBundle(MESSAGE_BASE, getLocale());
		String rbname = rb.getBaseBundleName();
		Enumeration<String> rbvalues = rb.getKeys();
		return MessageFormat.format(rb.getString(key), params);
	}

	public static String getTimestamp() {
		Long mili = Instant.now().toEpochMilli();
		return mili.toString();

	}

	public static String replaceUmlaute(String orig) {
		String result = orig;

		result = replaceZeichen(orig, UMLAUT_REPLACEMENTS);

		return result;
	}

	public static String replaceSonderzeichen(String orig) {
		String result = orig;

		result = orig.replaceAll("\\W", "_");

		// result = replaceZeichen(orig, SONDERZEICHEN_REPLACEMENTS);

		return result;
	}

	public static String replaceZeichen(String orig, String[][] replacements) {
		String result = orig;

		for (int i = 0; i < replacements.length; i++) {
			result = result.replace(replacements[i][0], replacements[i][1]);
		}

		return result;
	}

	public static void showErrorBox(DbContext ctx, String message) {
		new TextBox(ctx, Util.getMessage("main.exception.title"), message).show();
	}

	public static String readStringFromFile(File tempFile) throws FileNotFoundException, IOException {
		String filenameString = "";
		BufferedReader buffReader = new BufferedReader(new FileReader(tempFile));
		while (buffReader.ready()) {
			String line = buffReader.readLine();
			filenameString = filenameString + line;
		}
		buffReader.close();
		return filenameString;
	}

	public static ArrayList<String> readStringListFromFile(File tempFile) throws FileNotFoundException, IOException {
		ArrayList<String> filenameList = new ArrayList<String>();
		BufferedReader buffReader = new BufferedReader(new FileReader(tempFile));
		while (buffReader.ready()) {
			String line = buffReader.readLine();
			filenameList.add(line);
		}
		buffReader.close();
		return filenameList;
	}

	public static void writeStringtoFile(File tempFile, String filenameStringOut) throws IOException {
		BufferedWriter buffWriter = new BufferedWriter(new FileWriter(tempFile));
		buffWriter.write(filenameStringOut);
		buffWriter.close();
	}

	public static void writeStringtoFile(File tempFile, ArrayList<String> filenameListout) throws IOException {
		BufferedWriter buffWriter = new BufferedWriter(new FileWriter(tempFile));
		for (String filename : filenameListout) {
			buffWriter.append(filename + System.getProperty("line.separator"));
		}
		buffWriter.close();
	}

	public static File gettempFile(String verzeichnis, String praefix, String extension) throws IOException {
		File tmpverz = new File(verzeichnis);
		File tempFile;
		tempFile = File.createTempFile(praefix, "." + extension, tmpverz);
		return tempFile;
	}

}
