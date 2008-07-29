/**
 * $Id$
 * Copyright 2008 - Seth M. Fuller
 * 
 * License: Apache 2.0
 *
 */
package com.schema_diff.schema;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import java.util.ArrayList;

import org.schema_diff.database.DatabaseConnection;

/**
 * Describe class <code>ProcessSchema</code> here.
 *
 * @author Seth Fuller
 * @version 1.0
 */
public class ProcessSchema {
    public static String SCHEMA_TABLE_TYPES = "schema.table_types";
    public static String tableListDelim = ",";

    private ArrayList tableTypesList = new ArrayList();

    private String schemaName = null;
    private DatabaseConnection dbConn = null;
    private DatabaseMetaData metaData = null;

    private String catalogName = null;
    private String schemaPattern = null;
    private String tableNamePattern = null;
    private String[] tableTypes = null;

    public ProcessSchema(String schemaName) throws SQLException {
	dbConn = DatabaseConnection.getInstance();
	this.schemaName = schemaName;
	initMetaData();
    }

    protected void initMetaData() throws SQLException {
	metaData = dbConn.getDatabaseMetaData(schemaName);

    }

    public ResultSet getSchemaColumns(String tableName) throws SQLException {
	ResultSet rsColumns = null;

	if (null != metaData) {
	    rsColumns = metaData.

	}

	return rsColumns;
    }

    public ResultSet getSchemaTables() throws SQLException {
	ResultSet rsTables = null;

	if (null != metaData) {
	    rsTables = metaData.getTables(catalogName, schemaPattern,
					  tableNamePattern, 
					  getTableTypes());
	}

	return rsTables;
    }

    protected String[] getTableTypes() {
	if (null == tableTypes) {
	    tableTypes = getGeneralTableTypes();
	    if (null == tableTypes) {
		tableTypes = getSchemaTableTypes();
	    }
	}

	return tableTypes;

    }

    protected String[] getGeneralTableTypes() {
	String[] genTableTypes = null;
	String tableTypeList
	    = AppSupport.getProperty(ProcessSchema.SCHEMA_TABLE_TYPES);
	if (null != tableTypeList && !tableTypeList.trim().equals("")) {
	    genTableTypes = tableTypeList.split(ProcessSchema.tableListDelim);
	}
	return genTableTypes;
    }

    protected String[] getSchemaTableTypes() {
	String[] schemaTableTypes = null;
	String propName = schemaName + "."
	    + ProcessSchema.SCHEMA_TABLE_TYPES;
	String tableTypeList = AppSupport.getProperty(propName);
	if (null != tableTypeList && !tableTypeList.trim().equals("")) {
	    schemaTableTypes
		= tableTypeList.split(ProcessSchema.tableListDelim);
	}
	return schemaTableTypes;
    }

}