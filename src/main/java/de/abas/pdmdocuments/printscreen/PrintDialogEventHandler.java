package de.abas.pdmdocuments.printscreen;

import java.io.IOException;

import org.w3c.dom.events.EventException;

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
import de.abas.erp.db.schema.infrastructure.Printer;
import de.abas.erp.db.schema.infrastructure.SelectableInfrastructure;
import de.abas.erp.db.schema.printparameter.PrintDialogEditor;
import de.abas.erp.jfop.rt.api.annotation.RunFopWith;
import de.abas.jfop.base.buffer.BufferFactory;
import de.abas.jfop.base.buffer.ParentScreenBuffer;
import de.abas.pdmdocuments.infosystem.utils.Util;

@EventHandler(head = PrintDialogEditor.class)
@RunFopWith(EventHandlerRunner.class)
public class PrintDialogEventHandler {

	@FieldEventHandler(field = "printer", type = FieldEventType.EXIT)
	public void printerExit(FieldEvent event, ScreenControl screenControl, DbContext ctx, PrintDialogEditor head)
			throws EventException {

		fillAnhangList(ctx, head);

	}

	@ButtonEventHandler(field = "selectPrinter", type = ButtonEventType.AFTER, table = false)
	public void startAfter(ButtonEvent event, ScreenControl screenControl, DbContext ctx, PrintDialogEditor head)
			throws EventException {

		fillAnhangList(ctx, head);

	}

	@ScreenEventHandler(type = ScreenEventType.ENTER)
	public void screenEnter(ScreenEvent event, ScreenControl screenControl, DbContext ctx, PrintDialogEditor head)
			throws EventException {

		fillAnhangList(ctx, head);
	}

	private void fillAnhangList(DbContext ctx, PrintDialogEditor head) {
		SelectableInfrastructure selPrinter = head.getPrinter();

		if (selPrinter instanceof Printer) {
			Printer printer = (Printer) selPrinter;
			EnumPrinterType printerTyp = printer.getPrinterType();
			if (printerTyp.equals(EnumPrinterType.EmailClientSend) || printerTyp.equals(EnumPrinterType.EmailClientView)
					|| printerTyp.equals(EnumPrinterType.SMTPServer)) {
				try {
					head.setAttachmentFileList("");
					BufferFactory buffInst = BufferFactory.newInstance();
					ParentScreenBuffer parentbuffer = buffInst.getParentScreenBuffer();
//					if (parentbuffer.isVarDefined("ypdm01anhanglist")) {
//						String uebdatei = parentbuffer.getStringValue("ypdm01anhanglist");
//						if (uebdatei != null) {
//							if (!uebdatei.isEmpty()) {
//								head.setAttachmentFileList(uebdatei);
//							}
//						}
//
//					}
					if (head.getAttachmentFileList().isEmpty()) {
						head.setAttachmentFileList(Util.gettempFile("rmtmp", "pdmDoc", "TMP").toString());
					}

				} catch (IOException e) {
					Util.showErrorBox(ctx, "printscreen.error.tempFile");
				}

			}
		}
	}

}
