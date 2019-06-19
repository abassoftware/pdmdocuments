package de.abas.pdmdocuments.coffee.infosystem.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.itextpdf.text.pdf.PdfReader;

public final class DocumentsUtil {
	// Werte sind in px
	private static final Integer HEIGHTA5 = 595;
	private static final Integer WIDTHA5 = 420;
	private static final Integer HEIGHTA4 = 842;
	private static final Integer WIDTHA4 = 595;
	private static final Integer HEIGHTA3 = 1191;
	private static final Integer WIDTHA3 = 842;
	private static final Integer HEIGHTA2 = 1684;
	private static final Integer WIDTHA2 = 1191;
	private static final Integer HEIGHTA1 = 2384;
	private static final Integer WIDTHA1 = 1684;
	private static final Integer HEIGHTA0 = 3370;
	private static final Integer WIDTHA0 = 1684;

	private DocumentsUtil() {
		throw new IllegalStateException("Utility class");
	}

	public static String getPageFormat(File file) throws IOException {

		if (file.isFile()) {

			String fileExtension = getFileExtension(file);

			if (fileExtension.equals("pdf")) {
				return checkPageFormatPdf(file);
			} else if (fileExtension.equals("tif") || fileExtension.equals(".tiff")) {
				return checkPageFormatTiff(file);
			} else {
				return null;
			}

		} else {
			return null;
		}

	}

	private static String checkPageFormatTiff(File file) throws IOException {
		BufferedImage img = null;

		img = ImageIO.read(file);
		Integer height = img.getHeight();
		Integer width = img.getWidth();

		return checkFormatFromPX(height, width);

	}

	private static String checkPageFormatPdf(File file) throws IOException {
		PdfReader doc;
		doc = new PdfReader(file.toString());
		com.itextpdf.text.Rectangle size = doc.getPageSizeWithRotation(1);
		Float heightFloat = size.getHeight();
		Float widthFloat = size.getWidth();
		Integer height = heightFloat.intValue();
		Integer width = widthFloat.intValue();

		return checkFormatFromPX(height, width);
	}

	private static String checkFormatFromPX(Integer height, Integer width) {
		if (checkRange(height, HEIGHTA5) && checkRange(width, WIDTHA5)) {
			return "A5";
		} else if (checkRange(height, HEIGHTA4) && checkRange(width, WIDTHA4)) {
			return "A4";
		} else if (checkRange(height, HEIGHTA3) && checkRange(width, WIDTHA3)) {
			return "A3";
		} else if (checkRange(height, HEIGHTA2) && checkRange(width, WIDTHA2)) {
			return "A2";
		} else if (checkRange(height, HEIGHTA1) && checkRange(width, WIDTHA1)) {
			return "A1";
		} else if (checkRange(height, HEIGHTA0) && checkRange(width, WIDTHA0)) {
			return "A0";
		} else {
			return "SO";
		}
	}

	private static boolean checkRange(Integer value, Integer checkvalue) {
		Integer range = 10;

		int checkmin = checkvalue - range;
		int checkmax = checkvalue + range;

		return (value >= checkmin && value <= checkmax);

	}

	public static String getFileExtension(File file) {

		String filename = file.getName();
		return getFileExtensionFromName(filename);
	}

	public static String getFileExtensionFromName(String filename) {
		int pointIndex = filename.lastIndexOf('.');
		if (pointIndex != -1) {
			pointIndex = pointIndex + 1;
			return filename.substring(pointIndex);
		}

		return "";

	}
}
