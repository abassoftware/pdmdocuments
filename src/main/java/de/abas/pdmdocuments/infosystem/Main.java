package de.abas.pdmdocuments.infosystem;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.w3c.dom.events.EventException;

import de.abas.eks.jfop.annotation.Stateful;
import de.abas.eks.jfop.remote.FO;
import de.abas.eks.jfop.remote.FOe;
import de.abas.erp.api.session.ClientInformation;
import de.abas.erp.api.session.GUIInformation;
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
import de.abas.erp.db.DbContext;
import de.abas.erp.db.infosystem.custom.owpdm.PdmDocuments;
import de.abas.erp.db.infosystem.custom.owpdm.PdmDocuments.Row;
import de.abas.erp.db.schema.infrastructure.Printer;
import de.abas.erp.db.schema.userenums.UserEnumPdmSystems;
import de.abas.erp.jfop.rt.api.annotation.RunFopWith;
import de.abas.pdmdocuments.infosystem.config.Configuration;
import de.abas.pdmdocuments.infosystem.config.InitialConfigurationProcad;
import de.abas.pdmdocuments.infosystem.data.PdmDocument;
import de.abas.pdmdocuments.infosystem.utils.UtilwithAbasConnection;

@Stateful
@EventHandler(head = PdmDocuments.class, row = PdmDocuments.Row.class)
@RunFopWith(EventHandlerRunner.class)
public class Main {

	private ScreenOperations screenOperations = new ScreenOperations();
	protected static final Logger log = Logger.getLogger(Main.class);

	protected static final String SEPERATOR = System.getProperty("line.separator");
	protected List<String> maskenkontext = new ArrayList<>();
	private Configuration config = Configuration.getInstance();

	@ScreenEventHandler(type = ScreenEventType.ENTER)
	public void screenEnter(ScreenEvent event, ScreenControl screenControl, DbContext ctx, PdmDocuments head)
			throws EventException {

		ConfigurationMaskManager.getConfigInMask(head, ctx);
		this.screenOperations.showConfiguration(ctx, this.config);
	}

	@ButtonEventHandler(field = "start", type = ButtonEventType.AFTER, table = false)
	public void startAfter(ButtonEvent event, ScreenControl screenControl, DbContext ctx, PdmDocuments head)
			throws EventException {

		ConfigurationMaskManager.getConfigInMask(head, ctx);
		Boolean noErrorsInPDM = true;

		log.info(UtilwithAbasConnection.getMessage("pdmDocument.info.startvalues", head.getYartikel(), head.getYbeleg(),
				head.getYdrucker(), head.getYanhangliste()));

		setFieldsforPrinterTyp(head);

		if (head.getYartikel() != null || head.getYbeleg() != null) {

			head.table().clear();

			DocumentSearchfactory documentSearchfactory = new DocumentSearchfactory();

			try {
				SearchMaschineProduct.loadProductsInTable(head, ctx);
				DocumentsInterface searchdokuments = documentSearchfactory.create(config);

				head.setYanhangliste(AttachmentFileManager.getTempFileYanhangliste(head).getAbsolutePath());

				if (searchdokuments != null) {

					noErrorsInPDM = insertDocumentsInTable(ctx, head, noErrorsInPDM, searchdokuments);

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
			ClientInformation clientInfo = new ClientInformation(ctx);
			if (clientInfo.isGUIRunning()) {
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

	@FieldEventHandler(field = "ypdmsystem", type = FieldEventType.EXIT, table = false)
	public void ypdmsystemExit(FieldEvent event, ScreenControl screenControl, DbContext ctx, PdmDocuments head)
			throws EventException {

		UserEnumPdmSystems pdmsys = head.getYpdmsystem();

		if (pdmsys != null) {
			this.screenOperations.replacePDMSystemInMaskkontext(pdmsys);
			if (pdmsys.equals(UserEnumPdmSystems.PROFILE)) {
				InitialConfigurationProcad.preFillProFileFields(head);
			}

		}

	}

	@FieldEventHandler(field = "yauswahl", type = FieldEventType.EXIT, table = true)
	public void yauswahlExit(FieldEvent event, ScreenControl screenControl, DbContext ctx, PdmDocuments head,
			PdmDocuments.Row currentRow) throws EventException {

		try {
			if (currentRow.getYauswahl()) {
				AttachmentFileManager.addFilenameAtYuebdatei(currentRow.getYdateiname(), head);
				AttachmentFileManager.addPathAtAttachmentlist(currentRow.getYpfad(), head);
			} else {
				AttachmentFileManager.deleteFilenameAtYuebdatei(currentRow.getYdateiname(), head);
				AttachmentFileManager.deletePathAtAttachmentlist(currentRow.getYpfad(), head);
			}
		} catch (PdmDocumentsException e) {

			UtilwithAbasConnection.showErrorBox(ctx,
					UtilwithAbasConnection.getMessage("pdmDocument.error.yauswahl") + "/n" + e.getMessage());
		}

	}

	@ButtonEventHandler(field = "ysaveconfig", type = ButtonEventType.AFTER)
	public void ysaveconfigAfter(ButtonEvent event, ScreenControl screenControl, DbContext ctx, PdmDocuments head)
			throws EventException {

		ConfigurationMaskManager.saveconfig(ctx, head, this.config);

	}

	@ButtonEventHandler(field = "ybuanzeigen", type = ButtonEventType.AFTER, table = true)
	public void ybuanzeigenAfter(ButtonEvent event, ScreenControl screenControl, DbContext ctx, PdmDocuments head,
			PdmDocuments.Row currentRow) throws EventException {

		GUIInformation gui = new GUIInformation(ctx);
		File clientDir = gui.getClientTempDir();

		String zieldatvalue = clientDir.getPath() + "\\" + currentRow.getYdateiname();
		String valuecmd = " -PC -BIN " + currentRow.getYpfad() + " " + zieldatvalue;
		FO.pc_copy(valuecmd);
		FO.pc_open(zieldatvalue);

	}

	private Boolean insertDocumentsInTable(DbContext ctx, PdmDocuments head, Boolean noErrorsInPDM,
			DocumentsInterface searchdokuments) throws IOException, PdmDocumentsException {
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

		DocumentsFileManager.renameFiles(rows, ctx);

		AttachmentFileManager.writeAttachmentfile(rows, head);
		return noErrorsInPDM;
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
			fileNameList = AttachmentFileManager.getyuebDateiArray(head);
			newList = (ArrayList<PdmDocument>) pdmDocuments.stream()
					.filter(pdmDocument -> pdmDocument.checkFileNameList(fileNameList)).collect(Collectors.toList());
		} catch (IOException e) {
			log.error(e);
		}

		return newList;
	}

	private List<PdmDocument> checkDoktyp(ArrayList<PdmDocument> pdmDocuments, PdmDocuments head) {
		List<String> doctypList;

		doctypList = getydokartArray(head);

		return pdmDocuments.stream().filter(pdmDocument -> pdmDocument.checkDocTypList(doctypList))
				.collect(Collectors.toList());

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

	private List<String> getydokartArray(PdmDocuments head) {
		ArrayList<String> docArtArray = new ArrayList<>();
		if (!head.getYdokart().isEmpty()) {
			String[] docArtStringlist = head.getYdokart().split(";");
			for (String dokartString : docArtStringlist) {
				docArtArray.add(dokartString);
			}
			String[] docArtStringlist2 = head.getYdokartflex().split(";");
			for (String dokartString : docArtStringlist2) {
				docArtArray.add(dokartString);
			}
		}
		return docArtArray;
	}

	private void setFieldsforPrinterTyp(PdmDocuments head) {
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
	}

}
