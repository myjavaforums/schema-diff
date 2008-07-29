/**
 * $Id$
 *
 * Copyright 2008 - Seth M. Fuller
 * 
 * License: Apache 2.0
 *
 */
package org.schema_diff.main;

import java.util.Properties;
import java.util.regex.Pattern;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.schema_diff.diff.SchemaDiff;
import org.schema_diff.database.DatabaseConnection;

/**
 * This is the main function of the Schema Diff application.
 *
 */
public class SchemaDiffMain {

    private DatabaseConnection dbConn = null;

    private String schema1Name = null;
    private String schema2Name = null;

    private SchemaDiff schemaDiff = null;

    public SchemaDiffMain(String schema1Name, String schema2Name) {
	this.schema1Name = schema1Name;
	this.schema2Name = schema2Name;

	dbConn = DatabaseConnection.getInstance();

    }


    private void cleanUp() {
	if (null != dbConn) {
	    try {
		dbConn.closeAllConnections();
	    }
	    catch (Exception ex) {
		ex.printStackTrace();
	    }
	}
    }

    private void schemaDiff(String[] args) {
	int errorC = 0;
	if (null == schema1Name) {
	    System.err.println("Error Missing Schema 1");
	    errorC++;
	}
	if (null == schema2Name) {
	    System.err.println("Error Missing Schema 2");
	    errorC++;
	}
	if (errorC == 0) {
	    SchemaDiff schemaDiff
		= new SchemaDiff(schema1Name, schema2Name);
	    System.out.println("BEGIN Schema Diff\n");
	    try
		{
		    schemaDiff.runSchemaDiff();
		    System.out.println("END Schema Diff\n");
		}
	    catch (final Exception e) {
		// Print the stack trace for the benefit of the command line tools
		e.printStackTrace();
	    }
	}
	else {
	    System.err.println("SchemaDiff not running due "
			       + "to missing arguments");
	}
    }



    public static void main(String[] args) {
	SchemaDiffMain schemaDiffMain = null;
	String schema1Name = null;
	String schema2Name = null;

	if (args.length >= 2) {
	    schema1Name = args[0];
	    schema2Name = args[1];
	}

	schemaDiffMain = new SchemaDiffMain(schema1Name, schema2Name);
	schemaDiffMain.schemaDiff(args);

	schemaDiffMain.cleanUp();

	System.out.println("Done...");
    }
}
