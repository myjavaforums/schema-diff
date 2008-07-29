/**
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


import org.schema_diff.database.DatabaseConnection;
public class ProcessSchema {
    private String schemaName = null;
    private DatabaseConnection dbConn = null;
    private DatabaseMetaData metaData = null;

    public ProcessSchema(String schemaName) {
	dbConn = DatabaseConnection.getInstance();
	this.schemaName = schemaName;
    }

    protected initMetaData() {
	
    }

}