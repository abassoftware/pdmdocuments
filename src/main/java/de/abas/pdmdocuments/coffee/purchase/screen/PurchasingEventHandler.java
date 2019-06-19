package de.abas.pdmdocuments.coffee.purchase.screen;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.w3c.dom.events.EventException;

import de.abas.erp.api.AppContext;
import de.abas.erp.api.commands.CommandFactory;
import de.abas.erp.api.commands.FieldManipulator;
import de.abas.erp.axi.screen.ScreenControl;
import de.abas.erp.axi2.EventHandlerRunner;
import de.abas.erp.axi2.annotation.ButtonEventHandler;
import de.abas.erp.axi2.annotation.EventHandler;
import de.abas.erp.axi2.event.ButtonEvent;
import de.abas.erp.axi2.type.ButtonEventType;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.exception.CommandException;
import de.abas.erp.db.infosystem.custom.owpdm.PdmDocuments;
import de.abas.erp.db.schema.purchasing.PurchasingEditor;
import de.abas.erp.jfop.rt.api.annotation.RunFopWith;
import de.abas.pdmdocuments.coffee.infosystem.utils.Util;

@EventHandler(head = PurchasingEditor.class)

@RunFopWith(EventHandlerRunner.class)

public class PurchasingEventHandler {

	protected final static Logger log = Logger.getLogger(PurchasingEventHandler.class);

	@ButtonEventHandler(field = "ypdm01budocsammel", type = ButtonEventType.AFTER)
	public void ypdm01budocsammelAfter(ButtonEvent event, ScreenControl screenControl, DbContext ctx,
			PurchasingEditor head) throws EventException {
		try {

			checkTempFile(head, event, ctx);
			openPdmDocuments(head, ctx);
		} catch (IOException e) {
			Util.showErrorBox(ctx, Util.getMessage("purchasing.tempfile.error", e.getMessage()));
			log.error(e);
		} catch (CommandException e) {
			Util.showErrorBox(ctx, Util.getMessage("purchasing.tempfile.write.error", e.getMessage()));
			log.error(e);
		}

	}

	private void openPdmDocuments(PurchasingEditor head, DbContext ctx) {

		CommandFactory commandFactory = AppContext.createFor(ctx).getCommandFactory();

		FieldManipulator<PdmDocuments> fieldManipulator = commandFactory.getScrParamBuilder(PdmDocuments.class);
		fieldManipulator.setReference(PdmDocuments.META.ybeleg, head);
		// fieldManipulator.setField(PdmDocuments.META.yanhangliste,
		// head.getYpdm01anhanglist());
		fieldManipulator.pressButton(PdmDocuments.META.start);

		commandFactory.startInfosystem(PdmDocuments.class, fieldManipulator);

	}

	private void checkTempFile(PurchasingEditor head, ButtonEvent event, DbContext ctx)
			throws IOException, CommandException {

		// if (event.getCommand().equals(EnumEditorAction.Edit)) {
		// if (head.getYpdm01anhanglist().isEmpty()) {
		// head.setYpdm01anhanglist(Util.gettempFile("tmp", "pdmDocSammel",
		// "TMP").toString());
		// }
		// } else {
		//
		// if (head.getYpdm01anhanglist().isEmpty()) {
		// EditorCommand ediCommand =
		// EditorCommandFactory.create(EditorAction.MODIFY,
		// head.getId().toString());
		//
		// EditorObject editor = ctx.openEditor(ediCommand);
		//
		// editor.setString("ypdm01anhanglist", Util.gettempFile("tmp",
		// "pdmDocSammel", "TMP").toString());
		//
		// editor.commit();
		//
		// }
		//
		// }

	}

}
