package de.abas.pdmdocuments.infosystem.webservices.procad;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.abas.pdmdocuments.infosystem.PdmDocumentsException;
import de.abas.pdmdocuments.infosystem.config.Configuration;
import de.abas.pdmdocuments.infosystem.utils.UtilwithAbasConnection;

public class SQLConnectionHandler {

	private static final String PDM_DOCUMENT_ERROR_SQLCONNECTION_CONNECT = "pdmDocument.error.sqlconnection.connect";
	private Connection connection = null;
	Logger log = Logger.getLogger(SQLConnectionHandler.class);
	private String server;
	private String database;
	private Integer port;
	private String user;
	private String password;
	private String driver;

//	public Connection getConnection() throws PdmDocumentsException {
//		if (connection == null) {
//			openConnection();
//		} else
//			try {
//				if (connection.isClosed()) {
//					openConnection();
//				}
//			} catch (SQLException e) {
//				throw new PdmDocumentsException(
//						UtilwithAbasConnection.getMessage(PDM_DOCUMENT_ERROR_SQLCONNECTION_CONNECT), e);
//			}
//		return connection;
//	}

	public SQLConnectionHandler(Configuration config) throws PdmDocumentsException {

		this.server = config.getSqlServer();
		this.port = config.getSqlPort();
		this.database = config.getSqldatabase();
		this.driver = config.getSqlDriver();
		this.user = config.getSqlUser();
		this.password = config.getSqlPassword();

		openConnection();
		closeConnection();

	}

	private void openConnection() throws PdmDocumentsException {
		try {
			if (this.connection == null || this.connection.isClosed()) {
				Class.forName(this.driver);
				this.connection = DriverManager.getConnection(
						"jdbc:sqlserver://" + this.server + ":" + this.port + ";databasename=" + this.database,
						this.user, this.password);
				log.info(UtilwithAbasConnection.getMessage("pdmDocument.info.sqlconnection.connect"));
			}

		} catch (Exception e) {
			log.error(UtilwithAbasConnection.getMessage(PDM_DOCUMENT_ERROR_SQLCONNECTION_CONNECT), e);
			throw new PdmDocumentsException(UtilwithAbasConnection.getMessage(PDM_DOCUMENT_ERROR_SQLCONNECTION_CONNECT),
					e);
		}
	}

	public List<String> executeQuery(String sqlQuery) throws PdmDocumentsException {
		List<String> procaddocs = new ArrayList<>();

		openConnection();
		try (Statement sqlStatement = this.connection.createStatement();
				ResultSet result = sqlStatement.executeQuery(sqlQuery);) {
			while (result.next()) {
				String docID = result.getString("vb_objidnr2");
				procaddocs.add(docID);
			}
		} catch (SQLException e) {
			log.error(e);
			throw new PdmDocumentsException(
					UtilwithAbasConnection.getMessage("pdmDocument.error.sqlconnection.sqlerror"), e);
		} finally {
			closeConnection();
		}
		return procaddocs;

	}

	private void closeConnection() {
		try {
			if (this.connection != null && !this.connection.isClosed())
				this.connection.close();
		} catch (SQLException se) {
			log.error(se);
		}
	}
}
