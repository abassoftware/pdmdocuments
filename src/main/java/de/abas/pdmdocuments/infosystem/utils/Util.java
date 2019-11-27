package de.abas.pdmdocuments.infosystem.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class Util {

	private static final String MESSAGE_BASE = "de.abasgmbh.pdmDocuments.infosystem.messages";

	private static final String[][] UMLAUT_REPLACEMENTS = { { "Ä", "Ae" }, { "Ü", "Ue" }, { "Ö", "Oe" }, { "ä", "ae" },
			{ "ü", "ue" }, { "ö", "oe" }, { "ß", "ss" } };

	private Util() {
		throw new IllegalStateException("Utility class");
	}

	public static String getMessage(String key, Locale locale) {
		final ResourceBundle rb = ResourceBundle.getBundle(MESSAGE_BASE, locale);
		return rb.getString(key);
	}

	public static String getMessage(String key, Locale locale, Object... params) {
		final ResourceBundle rb = ResourceBundle.getBundle(MESSAGE_BASE, locale);
		return MessageFormat.format(rb.getString(key), params);
	}

	public static String getTimestamp() {
		Long mili = Instant.now().toEpochMilli();
		return mili.toString();

	}

	public static String replaceUmlaute(String orig) {

		return replaceZeichen(orig, UMLAUT_REPLACEMENTS);
	}

	public static String replaceSonderzeichen(String orig) {

		return orig.replaceAll("[^A-Za-z0-9._]", "_");
	}

	public static String replaceZeichen(String orig, String[][] replacements) {
		String result = orig;

		for (int i = 0; i < replacements.length; i++) {
			result = result.replace(replacements[i][0], replacements[i][1]);
		}
		return result;
	}

	public static String readStringFromFile(File tempFile) throws IOException {
		StringBuilder filenameBuilder = new StringBuilder();
		try (BufferedReader buffReader = new BufferedReader(new FileReader(tempFile))) {

			while (buffReader.ready()) {
				String line = buffReader.readLine();
				filenameBuilder.append(line);
			}
		}

		return filenameBuilder.toString();
	}

	public static List<String> readStringListFromFile(File tempFile) throws IOException {
		List<String> filenameList = new ArrayList<>();

		try (BufferedReader buffReader = new BufferedReader(new FileReader(tempFile))) {
			while (buffReader.ready()) {
				String line = buffReader.readLine();
				filenameList.add(line);
			}
		}

		return filenameList;
	}

	public static void writeStringtoFile(File tempFile, String filenameStringOut) throws IOException {

		try (BufferedWriter buffWriter = new BufferedWriter(new FileWriter(tempFile))) {
			buffWriter.write(filenameStringOut);
		}

	}

	public static void writeStringtoFile(File tempFile, List<String> filenameListout) throws IOException {

		try (BufferedWriter buffWriter = new BufferedWriter(new FileWriter(tempFile))) {
			for (String filename : filenameListout) {
				buffWriter.append(filename + System.getProperty("line.separator"));
			}
		}
	}

	public static File gettempFile(String verzeichnis, String praefix, String extension) throws IOException {
		File tmpverz = new File(verzeichnis);
		File tempFile;
		tempFile = File.createTempFile(praefix, "." + extension, tmpverz);
		return tempFile;
	}

}
