package de.abas.pdmdocuments.coffee.infosystem.webservices.procad;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import de.abas.pdmdocuments.coffee.infosystem.PdmDocumentsException;
import de.abas.pdmdocuments.coffee.infosystem.config.Configuration;
import de.abas.pdmdocuments.coffee.infosystem.utils.Util;

public class SQLConnectionHandler {

	private Connection connection = null;
	Logger log = Logger.getLogger(SQLConnectionHandler.class);
	private String server;
	private String database;
	private Integer port;
	private String user;
	private String password;
	private String driver;

	public Connection getConnection() throws PdmDocumentsException {
		if (connection == null) {
			openConnection();
		} else
			try {
				if (connection.isClosed()) {
					openConnection();
				}
			} catch (SQLException e) {
				throw new PdmDocumentsException(Util.getMessage("pdmDocument.error.sqlconnection.connect"), e);
			}
		return connection;
	}

	public SQLConnectionHandler(Configuration config) throws PdmDocumentsException {

		this.server = config.getSqlServer();
		this.port = config.getSqlPort();
		this.database = config.getSqldatabase();
		this.driver = config.getSqlDriver();
		this.user = config.getSqlUser();
		this.password = config.getSqlPassword();

		openConnection();
		try {
			this.connection.close();
		} catch (SQLException e) {
//				nichtsmachen;
		}

	}

	private void openConnection() throws PdmDocumentsException {
		try {
			Class.forName(this.driver);
			this.connection = DriverManager.getConnection(
					"jdbc:sqlserver://" + this.server + ":" + this.port + ";databasename=" + this.database, this.user,
					this.password);
			log.info(Util.getMessage("pdmDocument.info.sqlconnection.connect"));
		} catch (Exception e) {
			log.error(Util.getMessage("pdmDocument.error.sqlconnection.connect"), e);
			throw new PdmDocumentsException(Util.getMessage("pdmDocument.error.sqlconnection.connect"), e);
		}
	}

	public ArrayList<String> executeQuery(String sqlQuery) throws PdmDocumentsException {
		ArrayList<String> procaddocs = new ArrayList<String>();
		ResultSet result = null;
		Statement sqlStatement = null;
		;
		try {
			if (this.connection.isClosed()) {
				openConnection();
			}

			sqlStatement = this.connection.createStatement();
			result = sqlStatement.executeQuery(sqlQuery);
			while (result.next()) {
				String docID = result.getString("vb_objidnr2");
				procaddocs.add(docID);
			}
		} catch (SQLException e) {
			log.error(e);
			throw new PdmDocumentsException(Util.getMessage("pdmDocument.error.sqlconnection.sqlerror"), e);
		} finally {
			// finally block used to close resources
			try {
				if (sqlStatement != null)
					sqlStatement.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (this.connection != null)
					this.connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
		}
		return procaddocs;

	}
}
