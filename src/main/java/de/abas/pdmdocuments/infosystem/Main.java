package de.abas.pdmdocuments.infosystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.w3c.dom.events.EventException;

import de.abas.eks.jfop.annotation.Stateful;
import de.abas.eks.jfop.remote.FO;
import de.abas.eks.jfop.remote.FOe;
import de.abas.erp.api.session.GUIInformation;
import de.abas.erp.api.session.OperatorInformation;
import de.abas.erp.axi.screen.ScreenControl;
import de.abas.erp.axi2.EventHandlerRunner;
import de.abas.erp.axi2.annotation.ButtonEventHandler;
import de.abas.erp.axi2.annotation.EventHandler;
import de.abas.erp.axi2.annotation.FieldEventHandler;
import de.abas.erp.axi2.annotation.ScreenEventHandler;
import de.abas.erp.axi2.event.ButtonEvent;
import de.abas.erp.axi2.event.FieldEvent;
import de.abas.erp.axi2.event.ScreenEvent;
import de.abas.erp.axi2.type.ButtonEventType;
import de.abas.erp.axi2.type.FieldEventType;
import de.abas.erp.axi2.type.ScreenEventType;
import de.abas.erp.common.type.enums.EnumPrinterType;
import de.abas.erp.common.type.enums.EnumPriorities;
import de.abas.erp.db.ContextManager;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.SelectableObject;
import de.abas.erp.db.SelectableRecord;
import de.abas.erp.db.TableDescriptor;
import de.abas.erp.db.TableDescriptor.FieldQuantum;
import de.abas.erp.db.infosystem.custom.owpdm.PdmDocuments;
import de.abas.erp.db.infosystem.custom.owpdm.PdmDocuments.Row;
import de.abas.erp.db.infosystem.standard.st.MultiLevelBOM;
import de.abas.erp.db.schema.company.Password;
import de.abas.erp.db.schema.infrastructure.Printer;
import de.abas.erp.db.schema.part.Product;
import de.abas.erp.db.schema.part.SelectablePart;
import de.abas.erp.db.schema.purchasing.BlanketOrder;
import de.abas.erp.db.schema.purchasing.Invoice;
import de.abas.erp.db.schema.purchasing.PackingSlip;
import de.abas.erp.db.schema.purchasing.PurchaseOrder;
import de.abas.erp.db.schema.purchasing.Purchasing;
import de.abas.erp.db.schema.purchasing.Request;
import de.abas.erp.db.schema.sales.CostEstimate;
import de.abas.erp.db.schema.sales.Opportunity;
import de.abas.erp.db.schema.sales.Quotation;
import de.abas.erp.db.schema.sales.RepairOrder;
import de.abas.erp.db.schema.sales.Sales;
import de.abas.erp.db.schema.sales.SalesOrder;
import de.abas.erp.db.schema.sales.ServiceOrder;
import de.abas.erp.db.schema.sales.ServiceQuotation;
import de.abas.erp.db.schema.sales.WebOrder;
import de.abas.erp.db.schema.userenums.UserEnumPdmSystems;
import de.abas.erp.db.schema.workorder.WorkOrders;
import de.abas.erp.db.selection.Conditions;
import de.abas.erp.db.selection.ExpertSelection;
import de.abas.erp.db.selection.Selection;
import de.abas.erp.db.selection.SelectionBuilder;
import de.abas.erp.db.util.ContextHelper;
import de.abas.erp.jfop.rt.api.annotation.RunFopWith;
import de.abas.jfop.base.buffer.BufferFactory;
import de.abas.jfop.base.buffer.GlobalTextBuffer;
import de.abas.jfop.base.buffer.PrintBuffer;
import de.abas.pdmdocuments.infosystem.config.Configuration;
import de.abas.pdmdocuments.infosystem.config.ConfigurationHandler;
import de.abas.pdmdocuments.infosystem.data.PdmDocument;
import de.abas.pdmdocuments.infosystem.utils.Util;
import de.abas.pdmdocuments.infosystem.utils.UtilwithAbasConnection;

@Stateful
@EventHandler(head = PdmDocuments.class, row = PdmDocuments.Row.class)
@RunFopWith(EventHandlerRunner.class)
public class Main {

	private static final String MASKKONTEXTFOP = "maskkontextfop";
	private static final String DEVELOPER = "DEVELOPER";
	private static final String KONFIGURATION = "KONFIGURATION";
	private static final String PROFILE = "PROFILE";
	private static final String KEYTECH = "KEYTECH";
	private static final String COFFEE = "COFFEE";
	protected final static Logger log = Logger.getLogger(Main.class);
	protected final static String SQL_DRIVER_DEFAULT = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	protected final static Integer MAX_TREELEVEL = 999;
	// protected final static String SEPERATOR = ";";
	protected final static String SEPERATOR = System.getProperty("line.separator");
	protected List<String> maskenkontext = new ArrayList<>();
	private Configuration config = Configuration.getInstance();

	@ButtonEventHandler(field = "start", type = ButtonEventType.AFTER, table = false)
	public void startAfter(ButtonEvent event, ScreenControl screenControl, DbContext ctx, PdmDocuments head)
			throws EventException {

		getConfigInMask(head, ctx);
		Boolean noErrorsInPDM = true;

		log.info(UtilwithAbasConnection.getMessage("pdmDocument.info.startvalues", head.getYartikel(), head.getYbeleg(),
				head.getYdrucker(), head.getYanhangliste()));

		if (head.getYdrucker() != null) {
			head.setYistdrucker(isRealPrinter(head.getYdrucker()));
			head.setYistemail(isEmailPrinter(head.getYdrucker()));
		} else {
			head.setYistdrucker(false);
			head.setYistemail(false);
		}

		if (!(head.getYistdrucker() && head.getYistemail())) {
			head.setYistbildschirm(true);
		}

		if (head.getYartikel() != null || head.getYbeleg() != null) {

			head.table().clear();

			DocumentSearchfactory documentSearchfactory = new DocumentSearchfactory();

			try {
				loadProductsInTable(head, ctx);
				DocumentsInterface searchdokuments = documentSearchfactory.create(config);

				FileWriter fileWriter;
				File tempFile;
				tempFile = gettempFileAnhangListe(head);

				head.setYanhangliste(tempFile.getAbsolutePath());
				fileWriter = new FileWriter(tempFile);

				if (searchdokuments != null) {

					Iterable<Row> rows = head.table().getRows();
					for (Row row : rows) {
						if (row.getYtartikel() != null) {

							try {
								insertDocuments(row.getYtartikel().getIdno(), searchdokuments, head, ctx, row);
							} catch (PdmDocumentsException e) {
								StringReader strReader = new StringReader(e.getMessage());
								row.setYerror(strReader);
								noErrorsInPDM = false;
								log.error(e);
							}
						}
					}

					// nur Romaco
					renameFiles(rows, ctx);

					// Übergabe Dateien für den Druck anlegen.
					rows = head.table().getRows();
					for (Row row : rows) {
						if (!row.getYpfad().isEmpty()) {
							// Kopieren für FOPMULTI in das Output-Verzeichnis

							Printer printer = head.getYdrucker();
							if (printer != null) {
								if (isEmailPrinter(printer)) {
									// In die Dateianhangliste eintragen
									fileWriter.append(row.getYpfad() + System.getProperty("line.separator"));
								}
							}
						}

					}
					fileWriter.flush();
					fileWriter.close();

				} else {
					UtilwithAbasConnection.showErrorBox(ctx,
							UtilwithAbasConnection.getMessage("main.error.noConnection", head.getYserver()));
				}
			} catch (PdmDocumentsException | IOException e) {
				UtilwithAbasConnection.showErrorBox(ctx, e.getMessage());
			}
		} else {
			UtilwithAbasConnection.showErrorBox(ctx, UtilwithAbasConnection.getMessage("main.error.noProduct"));
		}

		if (!noErrorsInPDM) {
			BufferFactory buffact = BufferFactory.newInstance();
			GlobalTextBuffer textbuf = buffact.getGlobalTextBuffer();
			Boolean grafik = textbuf.getBooleanValue("grafik");
			if (grafik) {
				UtilwithAbasConnection.showErrorBox(ctx, UtilwithAbasConnection.getMessage("main.error.errorInTable"));
			} else {
				UtilwithAbasConnection.shownoticebar(ctx, screenControl,
						UtilwithAbasConnection.getMessage("main.error.errorInTable"));
			}
		}

		if (!head.getReportFoot().isEmpty()) {
			FOe.input(head.getReportFoot());
		}
	}

	private void renameFiles(Iterable<Row> rows, DbContext ctx) throws PdmDocumentsException {
		String number = "";
		String name = "";
		for (Row row : rows) {

			if (row.getYtartikel() != null) {
				number = row.getYtartikel().getIdno();
				name = row.getYtartikel().getDescr();
			}
			if (!row.getYpfad().isEmpty()) {
				String fileExtension = row.getYdatend();
				String oldPath = row.getYpfad();
				String newPath = oldPath.substring(0, oldPath.lastIndexOf("/"));
				String docId = "";
				if (row.getYmeta1key().equals("FileID")) {
					docId = row.getYmeta1value();
				} else if (row.getYdateiname().indexOf('_') != -1) {
					docId = row.getYdateiname().substring(0, row.getYdateiname().indexOf('_'));
				}

				String newFilename = number + "_" + name + "_" + docId;
				newFilename = Util.replaceUmlaute(newFilename.replaceAll(" ", "_"));
				newFilename = Util.replaceSonderzeichen(newFilename);
				newFilename = newFilename + "." + fileExtension;
				String newcompletePath = newPath + "/" + newFilename;
				File orgFile = new File(oldPath);
				File newFile = new File(newcompletePath);
				try {
					if (orgFile.exists()) {
						if (newFile.exists()) {
							Files.move(orgFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
						} else {
							Files.move(orgFile.toPath(), newFile.toPath(), StandardCopyOption.ATOMIC_MOVE);
						}
						actFileFields(row, newFilename, newcompletePath);
					} else {
						if (newFile.exists()) {
							actFileFields(row, newFilename, newcompletePath);
						}
					}

				} catch (IOException e) {
					throw new PdmDocumentsException(UtilwithAbasConnection.getMessage("error.renameFile.move"), e);
				}

			}
		}
	}

	private void actFileFields(Row row, String newFilename, String newcompletePath) {
		row.setYpfad(newcompletePath);
		row.setYdateiname(newFilename);
	}

	private boolean isEmailPrinter(Printer printer) {

		EnumPrinterType printertyp = printer.getPrinterType();
		if (printertyp == EnumPrinterType.EmailClientSend || printertyp == EnumPrinterType.EmailClientView) {
			log.info(UtilwithAbasConnection.getMessage("pdmDocument.info.is.emailprinter", printer.getSearchExt()));
			return true;
		} else {
			log.info(UtilwithAbasConnection.getMessage("pdmDocument.info.is.no.emailprinter", printer.getSearchExt()));
			return false;
		}

	}

	private boolean isRealPrinter(Printer printer) {

		EnumPrinterType printertyp = printer.getPrinterType();
		if (printertyp == EnumPrinterType.Printer || printertyp == EnumPrinterType.Terminal
				|| printertyp == EnumPrinterType.StandardWorkStationPrinter
				|| printertyp == EnumPrinterType.LocalPrinter) {
			log.info(UtilwithAbasConnection.getMessage("pdmDocument.info.is.realprinter", printer.getSearchExt()));
			return true;
		} else {
			log.info(UtilwithAbasConnection.getMessage("pdmDocument.info.is.no.realprinter", printer.getSearchExt()));
			return false;
		}

	}

	private File gettempFileAnhangListe(PdmDocuments head) throws IOException {

		String emailAttachmentFile = head.getYanhangliste();

		return getTempFile(emailAttachmentFile, "pdmDoc", ".TMP", "rmtmp");

	}

	private File getTempFile(String fileName, String praefix, String suffix, String tempVerz) throws IOException {

		File tmpverz = new File(tempVerz);
		File tempFile;

		if (fileName.isEmpty()) {
			tempFile = File.createTempFile(praefix, suffix, tmpverz);

			return tempFile;
		} else {
			tempFile = new File(fileName);
			if (!tempFile.exists()) {
				tempFile.createNewFile();
			}
			return tempFile;
		}

	}

	private void insertDocuments(String product, DocumentsInterface searchdokuments, PdmDocuments head, DbContext ctx,
			Row row) throws PdmDocumentsException, IOException {

		List<PdmDocument> pdmDocuments = searchdokuments.getAllDocuments(product, getFileTypList(head));

		ArrayList<PdmDocument> filtertDocuments1 = checkFilename(pdmDocuments, head);
		List<PdmDocument> filtertDocuments = checkDoktyp(filtertDocuments1, head);

		int rowIndex = row.getRowNo();
		for (PdmDocument pdmDocument : filtertDocuments) {

			rowIndex = rowIndex + 1;
			Row rowNew;
			if (rowIndex <= head.table().getRowCount()) {
				rowNew = head.table().insertRow(rowIndex);
			} else {
				rowNew = head.table().appendRow();
			}

			rowNew.setYdateiname(pdmDocument.getFilename());
			rowNew.setYdatend(pdmDocument.getFiletyp());
			if (pdmDocument.hasFile()) {
				rowNew.setYpfad(pdmDocument.getFile().getCanonicalPath());
			}
			rowNew.setYdoktyp(pdmDocument.getDocumenttyp());
			if (pdmDocument.hasError()) {
				Reader reader = new StringReader(pdmDocument.getError());
				rowNew.setYerror(reader);

			}
		}
	}

	private ArrayList<PdmDocument> checkFilename(List<PdmDocument> pdmDocuments, PdmDocuments head) {
		List<String> fileNameList;
		ArrayList<PdmDocument> newList = null;
		try {
			fileNameList = getyuebDateiArray(head);
			newList = (ArrayList<PdmDocument>) pdmDocuments.stream()
					.filter(pdmDocument -> pdmDocument.checkFileNameList(fileNameList)).collect(Collectors.toList());
		} catch (IOException e) {
			log.error(e);
		}

		return newList;
	}

	private List<PdmDocument> checkDoktyp(ArrayList<PdmDocument> pdmDocuments, PdmDocuments head) {
		List<String> doctypList;
		List<PdmDocument> newList = null;
		try {
			doctypList = getydokartArray(head);
			newList = (List<PdmDocument>) pdmDocuments.stream()
					.filter(pdmDocument -> pdmDocument.checkDocTypList(doctypList)).collect(Collectors.toList());
		} catch (IOException e) {
			log.error(e);
		}

		return newList;
	}

	private String[] getFileTypList(PdmDocuments head) {
		String drucktypen = head.getYdrucktypen();
		String emailtypen = head.getYemailtypen();
		String bildschirmtypen = head.getYbildschirmtypen();

		String[] drucktyplist = drucktypen.split(",");
		String[] emailtyplist = emailtypen.split(",");
		String[] bildschirmtyplist = bildschirmtypen.split(",");
		Printer printer = head.getYdrucker();
		if (printer != null) {
			if (isEmailPrinter(printer)) {
				return emailtyplist;
			}
			if (isRealPrinter(printer)) {
				return drucktyplist;
			}
		}
		return bildschirmtyplist;
	}

	// private boolean checkdocuments(PdmDocuments head, PdmDocument
	// pdmDocument, DbContext ctx) {
	//
	// String drucktypen = head.getYdrucktypen();
	// String emailtypen = head.getYemailtypen();
	// String bildschirmtypen = head.getYbildschirmtypen();
	//
	// String[] drucktyplist = drucktypen.split(",");
	// String[] emailtyplist = emailtypen.split(",");
	// String[] bildschirmtyplist = bildschirmtypen.split(",");
	//
	// Printer printer = getactPrinter(head, ctx);
	//
	// if (printer == null) {
	// printer = head.getYdrucker();
	// if (printer != null) {
	// return checkPrinter(head, pdmDocument, drucktyplist, emailtyplist,
	// bildschirmtyplist, printer);
	// } else {
	// if (checkDocumentString(pdmDocument, bildschirmtyplist)
	// && checkDocumenttyp(pdmDocument, head.getYdokart())) {
	// return true;
	// } else
	// return false;
	// }
	// } else {
	// return checkPrinter(head, pdmDocument, drucktyplist, emailtyplist,
	// bildschirmtyplist, printer);
	// }
	//
	// }

	// private Boolean checkPrinter(PdmDocuments head, PdmDocument pdmDocument,
	// String[] drucktyplist,
	// String[] emailtyplist, String[] bildschirmtyplist, Printer printer2) {
	// // EnumPrinterType printertyp = printer2.getPrinterType();
	//
	// if (isRealPrinter(printer2)) {
	//
	// if (checkDocumentString(pdmDocument, drucktyplist) &&
	// checkDocumenttyp(pdmDocument, head.getYdokart())) {
	//
	// return true;
	// } else {
	// return false;
	// }
	//
	// } else if (isEmailPrinter(printer2)) {
	//
	// if (checkDocumentString(pdmDocument, emailtyplist) &&
	// checkDocumenttyp(pdmDocument, head.getYdokart())) {
	//
	// return true;
	// } else {
	// return false;
	// }
	// } else {
	// if (checkDocumentString(pdmDocument, bildschirmtyplist)
	// && checkDocumenttyp(pdmDocument, head.getYdokart())) {
	//
	// return true;
	// } else {
	// return false;
	// }
	// }
	//
	// }

	// private boolean checkDocumenttyp(PdmDocument pdmDocument, String
	// documentart) {
	//
	// if (!documentart.isEmpty()) {
	// if (pdmDocument.getDocumenttyp().equals(documentart)) {
	// return true;
	// } else {
	// return false;
	// }
	// } else {
	// return true;
	// }
	//
	// }

	// private boolean checkDocumentString(PdmDocument pdmDocument, String[]
	// typlist) {
	//
	// String filetyp = pdmDocument.getFiletyp();
	// Boolean allempty = true;
	// // boolean test = typlist.toString().isEmpty();
	// // int anz = typlist.length;
	//
	// if (!typlist.toString().isEmpty()) {
	// for (String typ : typlist) {
	// if (!typ.isEmpty()) {
	// if (typ.trim().toUpperCase().equals(filetyp.toUpperCase())) {
	// log.trace(UtilwithAbasConnection.getMessage("pdmDocument.checkdocument.includePdmDoc",
	// pdmDocument.getFilename(),
	// Arrays.toString(typlist)));
	// return true;
	// }
	// allempty = false;
	// }
	// }
	// }
	//
	// if (allempty) {
	// log.trace(UtilwithAbasConnection.getMessage("pdmDocument.checkdocument.includePdmDoc.emptyTypliste",
	// pdmDocument.getFilename()));
	// return true;
	// } else {
	// log.trace(UtilwithAbasConnection.getMessage("pdmDocument.checkdocument.excludePdmDoc",
	// pdmDocument.getFilename(),
	// Arrays.toString(typlist)));
	// return false;
	// }
	// }

	@ButtonEventHandler(field = "ysaveconfig", type = ButtonEventType.AFTER)
	public void ysaveconfigAfter(ButtonEvent event, ScreenControl screenControl, DbContext ctx, PdmDocuments head)
			throws EventException {

		Configuration config = Configuration.getInstance();
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

	@ButtonEventHandler(field = "ybuanzeigen", type = ButtonEventType.AFTER, table = true)
	public void ybuanzeigenAfter(ButtonEvent event, ScreenControl screenControl, DbContext ctx, PdmDocuments head,
			PdmDocuments.Row currentRow) throws EventException {

		GUIInformation gui = new GUIInformation(ctx);
		File clientDir = gui.getClientTempDir();
		// gP.getStringValue("cltempdir");

		String zieldatvalue = clientDir.getPath() + "\\" + currentRow.getYdateiname();
		String valuecmd = " -PC -BIN " + currentRow.getYpfad() + " " + zieldatvalue;
		FO.pc_copy(valuecmd);
		FO.pc_open(zieldatvalue);

	}

	@FieldEventHandler(field = "yauswahl", type = FieldEventType.EXIT, table = true)
	public void yauswahlExit(FieldEvent event, ScreenControl screenControl, DbContext ctx, PdmDocuments head,
			PdmDocuments.Row currentRow) throws EventException {
		try {
			if (currentRow.getYauswahl()) {
				// addFilenameAtYuebfile(currentRow.getYdateiname(), head);
				addFilenameAtYuebdatei(currentRow.getYdateiname(), head);
				addPathAtAttachmentlist(currentRow.getYpfad(), head);
			} else {
				deleteFilenameAtYuebdatei(currentRow.getYdateiname(), head);
				deletePathAtAttachmentlist(currentRow.getYpfad(), head);
			}
		} catch (PdmDocumentsException e) {

			UtilwithAbasConnection.showErrorBox(ctx,
					UtilwithAbasConnection.getMessage("pdmDocument.error.yauswahl") + "/n" + e.getMessage());
		}

	}

	@FieldEventHandler(field = "ypdmsystem", type = FieldEventType.EXIT, table = false)
	public void ypdmsystemExit(FieldEvent event, ScreenControl screenControl, DbContext ctx, PdmDocuments head)
			throws EventException {

		UserEnumPdmSystems pdmsys = head.getYpdmsystem();

		if (pdmsys != null) {
			replacePDMSystemInMaskkontext(pdmsys);
			if (pdmsys.equals(UserEnumPdmSystems.PROFILE)) {

				if (checkFieldnameFieldsAreEmpty(head)) {
					preFillProFileFields(head);
				}
			}

		}

	}

	private void preFillProFileFields(PdmDocuments head) {
		head.setYfieldfornumber("/Part/pdmPartItemNumber");
		head.setYfieldforpartid("/Part/pdmPartID");
		head.setYfieldfororgname("/Document/orgName");
		head.setYfieldfordocversid("/Document/docVersionBaseId");
		head.setYfieldfordoctype("/Document/docType");

		head.setYsqldriver("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	}

	private boolean checkFieldnameFieldsAreEmpty(PdmDocuments head) {
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

	private void deletePathAtAttachmentlist(String ypfad, PdmDocuments head) throws PdmDocumentsException {
		try {
			File tempFile = getTempFileYanhangliste(head);

			deleteFilenametoFile(tempFile, ypfad);

		} catch (IOException e) {
			log.error(e);
			throw new PdmDocumentsException(UtilwithAbasConnection.getMessage("error.create.Tempfile", e.getMessage()));

		}
	}

	private void addPathAtAttachmentlist(String ypfad, PdmDocuments head) throws PdmDocumentsException {
		try {
			File tempFile = getTempFileYanhangliste(head);

			addFilenametoFile(tempFile, ypfad);

		} catch (IOException e) {
			log.error(e);
			throw new PdmDocumentsException(UtilwithAbasConnection.getMessage("error.create.Tempfile", e.getMessage()));
		}

	}

	private void deleteFilenameAtYuebdatei(String ydateiname, PdmDocuments head) throws PdmDocumentsException {
		try {
			File tempFile = getTempFileYuebdatei(head);

			deleteFilenametoFile(tempFile, ydateiname);

		} catch (IOException e) {
			log.error(e);
			throw new PdmDocumentsException(UtilwithAbasConnection.getMessage("error.create.Tempfile", e.getMessage()));

		}

	}

	private void deleteFilenametoFile(File tempFile, String ydateiname) throws FileNotFoundException, IOException {
		String filenameString = "";

		List<String> fileListArray = Util.readStringListFromFile(tempFile);

		if (fileListArray.contains(ydateiname)) {
			fileListArray.remove(ydateiname);
		}
		//
		// String filenameStringOut = convertStringArraytoString(fileListArray,
		// SEPERATOR);
		// Util.writeStringtoFile(tempFile, filenameStringOut);
		Util.writeStringtoFile(tempFile, fileListArray);

	}

	private void addFilenameAtYuebdatei(String ydateiname, PdmDocuments head) throws PdmDocumentsException {
		try {
			File tempFile = getTempFileYuebdatei(head);

			addFilenametoFile(tempFile, ydateiname);

		} catch (IOException e) {
			log.error(e);
			throw new PdmDocumentsException(UtilwithAbasConnection.getMessage("error.create.Tempfile", e.getMessage()));

		}

	}

	private void addFilenametoFile(File tempFile, String ydateiname) throws IOException {
		// String filenameString = "";

		List<String> fileListArray = Util.readStringListFromFile(tempFile);

		if (!fileListArray.contains(ydateiname)) {
			fileListArray.add(ydateiname);
		}

		// String filenameStringOut = convertStringArraytoString(fileListArray,
		// SEPERATOR);
		// Util.writeStringtoFile(tempFile, filenameStringOut);
		Util.writeStringtoFile(tempFile, fileListArray);
	}

	private File getTempFileYanhangliste(PdmDocuments head) throws IOException {
		File tempFile = null;
		String yanhangliste = head.getYanhangliste();

		if (yanhangliste.isEmpty()) {
			tempFile = getTempFile("", "pdmDocUeb", ".TMP", "rmtmp");
			head.setYuebdatei(tempFile.getAbsolutePath());
		} else {
			tempFile = getTempFile(yanhangliste, "pdmDocUeb", ".TMP", "rmtmp");
		}

		return tempFile;
	}

	private File getTempFileYuebdatei(PdmDocuments head) throws IOException {
		File tempFile = null;
		String yuebdatei = head.getYuebdatei();

		if (yuebdatei.isEmpty()) {
			tempFile = getTempFile("", "pdmDocUeb", ".TMP", "rmtmp");
			head.setYuebdatei(tempFile.getAbsolutePath());
		} else {
			tempFile = getTempFile(yuebdatei, "pdmDocUeb", ".TMP", "rmtmp");
		}

		return tempFile;
	}
	// private void deleteFilenamFromYuebfile(String ydateiname, PdmDocuments
	// head) {
	//
	// Writer writer = new StringWriter();
	// try {
	// ArrayList<String> fileListArray = getyuebFileArray(head);
	// fileListArray.remove(ydateiname);
	//
	// String output = convertStringArraytoString(fileListArray, separator);
	// Reader reader = new StringReader(output);
	// head.setYuebfile(reader);
	// } catch (IOException e) {
	// log.error(e);
	// }
	//
	// }

	private List<String> getyuebDateiArray(PdmDocuments head) throws IOException {
		List<String> fileListArray = new ArrayList<String>();
		if (!head.getYuebdatei().isEmpty()) {
			File tempFile = new File(head.getYuebdatei());
			// String stringfromFile = Util.readStringFromFile(tempFile);
			// fileListArray = convertStringtoStringArray(stringfromFile,
			// SEPERATOR);
			fileListArray = Util.readStringListFromFile(tempFile);
		}
		return fileListArray;
	}

	private List<String> getydokartArray(PdmDocuments head) throws IOException {
		ArrayList<String> docArtArray = new ArrayList<String>();
		if (!head.getYdokart().isEmpty()) {
			String[] docArtStringlist = head.getYdokart().split(";");
			for (String dokartString : docArtStringlist) {
				docArtArray.add(dokartString);
			}

		}
		return docArtArray;
	}

	@ScreenEventHandler(type = ScreenEventType.ENTER)
	public void screenEnter(ScreenEvent event, ScreenControl screenControl, DbContext ctx, PdmDocuments head)
			throws EventException {

		getConfigInMask(head, ctx);
		showConfiguration(ctx);
	}

	private void check_Screen(DbContext ctx) {

		OperatorInformation operatorInformation = new OperatorInformation(ctx);
		Password passw = operatorInformation.getPwdRecord();
		EnumPriorities prio = passw.getPrio();

		if (prio.compareTo(EnumPriorities.E) >= 0) {
			addMaskkontext(KONFIGURATION);
		}

	}

	private void addMaskkontext(String konfiguration) {

		this.maskenkontext.add(konfiguration);
		actMaskContext();
	}

	private void removeMaskkontext(String konfiguration) {

		this.maskenkontext.remove(konfiguration);
		actMaskContext();
	}

	private void replacePDMSystemInMaskkontext(UserEnumPdmSystems pdmSystem) {
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

	private void showConfiguration(DbContext ctx) {

		check_Screen(ctx);

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

	private void getConfigInMask(PdmDocuments head, DbContext ctx) {

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

			// Vorbelegung für SQL-Server falls noch nicht gespeichert
			if (head.getYsqldriver().isEmpty()) {
				head.setYsqldriver(Main.SQL_DRIVER_DEFAULT);
				config.setSqlDriver(Main.SQL_DRIVER_DEFAULT);
			}

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

	private int checknull(Integer value) {
		if (value != null) {
			return value;
		} else {
			return 0;
		}

	}

	private String checknull(String value) {

		if (value != null) {
			return value;
		} else {
			return "";
		}

	}

	private void loadProductsInTable(PdmDocuments head, DbContext ctx) throws PdmDocumentsException {

		if (head.getYbeleg() == null) {

			if (head.getYartikel() != null) {
				insertProductInRow(head.getYartikel(), head);
			}

		} else {
			SelectableObject beleg = head.getYbeleg();
			ArrayList<Product> listProduct = getProducts(beleg, ctx);
			for (Product product : listProduct) {
				insertProductInRow(product, head);
			}

		}

		if (head.getYstruktur()) {
			List<Row> tableRows = head.getTableRows();
			for (Row row : tableRows) {
				if (row.getYtstufe() == 1) {
					insertProductStructureInRow(head, row);
				}

			}
		}

	}

	private Iterable<Product> getProductsFromString(String productString, DbContext ctx) throws PdmDocumentsException {
		// trenner ;
		ArrayList<Product> productList = new ArrayList<Product>();

		String[] productStringlist = productString.split(";");
		for (String productNumber : productStringlist) {
			SelectionBuilder<Product> selectionBuilder = SelectionBuilder.create(Product.class);
			selectionBuilder.add(Conditions.eq(Product.META.idno, productNumber));
			de.abas.erp.db.Query<Product> queryproduct = ctx.createQuery(selectionBuilder.build());
			List<Product> productQueryList = queryproduct.execute();

			if (productQueryList.size() == 0) {
				throw new PdmDocumentsException(
						UtilwithAbasConnection.getMessage("error.sammellist.productnotfound", productNumber));
			}
			if (productQueryList.size() > 1) {
				throw new PdmDocumentsException(
						UtilwithAbasConnection.getMessage("error.sammellist.productnotunique", productNumber));
			}
			productList.add(productQueryList.get(0));

		}

		return productList;
	}

	private ArrayList<Product> getProducts(SelectableObject beleg, DbContext ctx) {
		ArrayList<Product> listProduct = new ArrayList<Product>();

		if (beleg instanceof Sales) {

			listProduct = productsfromSalesBlanketOrder(beleg, ctx);
			if (listProduct.isEmpty()) {
				listProduct = productsfromSalesQuotation(beleg, ctx);
			}

			if (listProduct.isEmpty()) {
				listProduct = productsfromSalesOrder(beleg, ctx);
			}

			if (listProduct.isEmpty()) {
				listProduct = productsfromSalesInvoice(beleg, ctx);
			}

			if (listProduct.isEmpty()) {
				listProduct = productsfromSalesInvoice(beleg, ctx);
			}
			if (listProduct.isEmpty()) {
				listProduct = productsfromSalesWebOrder(beleg, ctx);
			}

			if (listProduct.isEmpty()) {
				listProduct = productsfromSalesServiceQuotation(beleg, ctx);
			}

			if (listProduct.isEmpty()) {
				listProduct = productsfromSalesServiceOrder(beleg, ctx);
			}

			if (listProduct.isEmpty()) {
				listProduct = productsfromSalesRepairOrder(beleg, ctx);
			}

			if (listProduct.isEmpty()) {
				listProduct = productsfromSalesOpportunity(beleg, ctx);
			}

			if (listProduct.isEmpty()) {
				listProduct = productsfromSalesCostEstimate(beleg, ctx);
			}

		} else if (beleg instanceof Purchasing) {

			listProduct = productsfromPurchaseBlanketOrder(beleg, ctx);

			if (listProduct.isEmpty()) {
				listProduct = productsfromPurchaseOrder(beleg, ctx);
			}

			if (listProduct.isEmpty()) {
				listProduct = productsfromPurchaseOrder(beleg, ctx);
			}

			if (listProduct.isEmpty()) {
				listProduct = productsfromRequest(beleg, ctx);
			}

			if (listProduct.isEmpty()) {
				listProduct = productsfromPurchasePackingSlip(beleg, ctx);
			}

			if (listProduct.isEmpty()) {
				listProduct = productsfromPurchaseInvoice(beleg, ctx);
			}

		} else if (beleg instanceof WorkOrders) {
			WorkOrders sbeleg = (WorkOrders) beleg;
			Product product = getProduct(sbeleg.getProduct());
			if (product != null) {
				listProduct.add(product);
			}

		}

		return listProduct;
	}

	private ArrayList<Product> productsfromPurchaseOrder(SelectableObject beleg, DbContext ctx) {

		int database = 4;
		int group = 2;

		return getProductsFromSalesOrPurchase(beleg, ctx, database, group, PurchaseOrder.Row.META.product.getName());
	}

	private ArrayList<Product> productsfromSalesOrder(SelectableObject beleg, DbContext ctx) {
		int database = 3;
		int group = 2;

		return getProductsFromSalesOrPurchase(beleg, ctx, database, group, SalesOrder.Row.META.product.getName());

	}

	private ArrayList<Product> productsfromSalesWebOrder(SelectableObject beleg, DbContext ctx) {
		int database = 3;
		int group = 2;

		return getProductsFromSalesOrPurchase(beleg, ctx, database, group, WebOrder.Row.META.product.getName());

	}

	private ArrayList<Product> productsfromPurchaseBlanketOrder(SelectableObject beleg, DbContext ctx) {
		int database = 4;
		int group = 2;

		return getProductsFromSalesOrPurchase(beleg, ctx, database, group, BlanketOrder.Row.META.product.getName());

	}

	private ArrayList<Product> productsfromSalesQuotation(SelectableObject beleg, DbContext ctx) {
		int database = 3;
		int group = 2;

		return getProductsFromSalesOrPurchase(beleg, ctx, database, group, Quotation.Row.META.product.getName());
	}

	private ArrayList<Product> productsfromSalesCostEstimate(SelectableObject beleg, DbContext ctx) {
		int database = 3;
		int group = 2;

		return getProductsFromSalesOrPurchase(beleg, ctx, database, group, CostEstimate.Row.META.product.getName());
	}

	private ArrayList<Product> productsfromSalesOpportunity(SelectableObject beleg, DbContext ctx) {
		int database = 3;
		int group = 2;

		return getProductsFromSalesOrPurchase(beleg, ctx, database, group, Opportunity.Row.META.product.getName());
	}

	private ArrayList<Product> productsfromSalesServiceQuotation(SelectableObject beleg, DbContext ctx) {
		int database = 3;
		int group = 2;

		return getProductsFromSalesOrPurchase(beleg, ctx, database, group, ServiceQuotation.Row.META.product.getName());
	}

	private ArrayList<Product> productsfromSalesServiceOrder(SelectableObject beleg, DbContext ctx) {
		int database = 3;
		int group = 2;

		return getProductsFromSalesOrPurchase(beleg, ctx, database, group, ServiceOrder.Row.META.product.getName());
	}

	private ArrayList<Product> productsfromSalesRepairOrder(SelectableObject beleg, DbContext ctx) {
		int database = 3;
		int group = 2;

		return getProductsFromSalesOrPurchase(beleg, ctx, database, group, RepairOrder.Row.META.product.getName());
	}

	private ArrayList<Product> productsfromSalesBlanketOrder(SelectableObject beleg, DbContext ctx) {
		int database = 3;
		int group = 2;

		return getProductsFromSalesOrPurchase(beleg, ctx, database, group,
				de.abas.erp.db.schema.sales.BlanketOrder.Row.META.product.getName());
	}

	private ArrayList<Product> productsfromPurchasePackingSlip(SelectableObject beleg, DbContext ctx) {
		int database = 4;
		int group = 2;

		return getProductsFromSalesOrPurchase(beleg, ctx, database, group, PackingSlip.Row.META.product.getName());
	}

	private ArrayList<Product> productsfromRequest(SelectableObject beleg, DbContext ctx) {
		int database = 4;
		int group = 2;

		return getProductsFromSalesOrPurchase(beleg, ctx, database, group, Request.Row.META.product.getName());
	}

	private ArrayList<Product> productsfromPurchaseInvoice(SelectableObject beleg, DbContext ctx) {
		int database = 4;
		int group = 2;

		return getProductsFromSalesOrPurchase(beleg, ctx, database, group, Invoice.Row.META.product.getName());
	}

	private ArrayList<Product> productsfromSalesInvoice(SelectableObject beleg, DbContext ctx) {
		int database = 3;
		int group = 2;

		return getProductsFromSalesOrPurchase(beleg, ctx, database, group,
				de.abas.erp.db.schema.sales.Invoice.Row.META.product.getName());
	}

	private Product getProduct(SelectablePart selProduct) {
		if (selProduct instanceof Product) {
			return (Product) selProduct;
		} else {
			return null;
		}
	}

	private ArrayList<Product> getProductsFromSalesOrPurchase(SelectableObject beleg, DbContext ctx, int database,
			int group, String productFieldName) {

		ArrayList<Product> listProduct = new ArrayList<Product>();
		String criteria = "head=" + beleg.getId().toString() + ";@filingmode=(Both)";
		log.info(UtilwithAbasConnection.getMessage("start.selection.SalesOrPurchase", System.currentTimeMillis(),
				criteria));
		Selection<? extends SelectableRecord> selectionStorage = ExpertSelection
				.create(new TableDescriptor(database, group, FieldQuantum.Table), criteria);
		de.abas.erp.db.Query<? extends SelectableRecord> query = ctx.createQuery(selectionStorage);
		String crittest = selectionStorage.getCriteria();
		for (SelectableRecord row : query) {
			String productString = row.getString(productFieldName);
			Product product = getProduct(productString, ctx);
			if (product != null) {
				listProduct.add(product);
			}
		}
		log.info(UtilwithAbasConnection.getMessage("end.selection.SalesOrPurchase", System.currentTimeMillis(),
				criteria));
		return listProduct;
	}

	private Product getProduct(String productString, DbContext ctx) {
		String criteria = "id=" + productString + ";@file=2:1";
		Selection<Product> select = ExpertSelection.create(Product.class, criteria);
		de.abas.erp.db.Query<Product> queryProduct = ctx.createQuery(select);
		Product productsel = null;
		for (Product product : queryProduct) {
			if (productsel == null) {
				productsel = product;
			} else {
				// Es darf nur einen Treffer geben.
				return null;
			}

		}

		return productsel;
	}

	private void insertProductStructureInRow(PdmDocuments head, Row row) {
		int aktrow = row.getRowNo() + 1;
		int treelevel = row.getYtstufe();
		int sstltreelevel = head.getYstufe();
		Product product = row.getYtartikel();
		if (sstltreelevel == 0) {
			sstltreelevel = MAX_TREELEVEL;
		}

		ArrayList<ProductListitem> productListitemList = getbomproducts(product, sstltreelevel);

		for (ProductListitem productListitem : productListitemList) {
			Row insertRow = head.table().insertRow(aktrow);
			aktrow++;
			insertRow.setYtartikel(productListitem.getProduct());
			insertRow.setYtstufe(productListitem.getStufe() + treelevel);
		}
	}

	private ArrayList<ProductListitem> getbomproducts(Product product, Integer maxStufe) {
		ArrayList<ProductListitem> productList = new ArrayList<ProductListitem>();
		ContextManager contextmanager = ContextHelper.buildContextManager();
		DbContext dbcontext = contextmanager.getServerContext();
		MultiLevelBOM mlb = dbcontext.openInfosystem(MultiLevelBOM.class);
		mlb.setArtikel(product);
		mlb.setCountLevels(maxStufe);
		mlb.setBmitag(false);
		mlb.setBmitfm(false);
		mlb.invokeStart();
		Iterable<de.abas.erp.db.infosystem.standard.st.MultiLevelBOM.Row> mlbRows = mlb.table().getRows();
		for (de.abas.erp.db.infosystem.standard.st.MultiLevelBOM.Row row : mlbRows) {
			int treeLevel = row.getTreeLevel();
			SelectableObject selProduct = row.getElem();
			if (selProduct instanceof Product) {
				Product productInPosition = (Product) selProduct;
				ProductListitem productListitem = new ProductListitem(productInPosition, treeLevel);
				productList.add(productListitem);
			}
		}
		mlb.close();
		return productList;

	}

	private void insertProductInRow(Product product, PdmDocuments head) {
		Row row = head.table().appendRow();
		row.setYtartikel(product);
		row.setYtstufe(1);
	}

}
