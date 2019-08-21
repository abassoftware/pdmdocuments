package de.abas.pdmdocuments.printlayout;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.apache.log4j.Logger;

import de.abas.erp.axi2.EventHandlerRunner;
import de.abas.erp.jfop.rt.api.annotation.RunFopWith;
import de.abas.jfop.base.buffer.BufferFactory;
import de.abas.jfop.base.buffer.PrintBuffer;
import de.abas.jfop.base.buffer.ScreenBuffer;
import de.abas.pdmdocuments.infosystem.utils.UtilwithAbasConnection;
import de.abas.print.engine.DataGenerator;

@RunFopWith(EventHandlerRunner.class)
public class FopMultiLayoutPrintAfter implements DataGenerator {
	Logger logger = Logger.getLogger(FopMultiLayoutPrintAfter.class);

	@Override
	public int startDatagen() {
		BufferFactory bufferFactory = BufferFactory.newInstance(true);
		PrintBuffer printBuffer = bufferFactory.getPrintBuffer();
		ScreenBuffer screenBuf = bufferFactory.getScreenBuffer();
		if (screenBuf.getBooleanValue("yistdrucker")) {
			while (screenBuf.hasNextRow()) {
				screenBuf.nextRow();
				String maskYpfad = screenBuf.getStringValue("ypfad");
				if (!maskYpfad.isEmpty()) {
					String aktttmp = printBuffer.getStringValue("actTempDir");

					String outputTempDir = aktttmp + "output";
					File file = new File(maskYpfad);
					File outputDir = new File(outputTempDir, file.getName());

					try {
						logger.info(UtilwithAbasConnection.getMessage("pdmdocuments.log.copyfile",
								file.toPath().toString(), outputDir.toPath().toString()));
						Files.copy(file.toPath(), outputDir.toPath(), StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException e) {
						logger.error(e);
						logger.error(UtilwithAbasConnection.getMessage("main.error.copyFile", e.getMessage()));
						return 1;
					}
				}
			}
		}
		return 0;
	}
}
