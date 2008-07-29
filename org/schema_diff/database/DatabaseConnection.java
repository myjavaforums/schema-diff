/**
 * Copyright 2008 - Seth M. Fuller
 * 
 * License: Apache 2.0
 *
 */
package org.schema_diff.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.schema_diff.AppSupport;

public class DatabaseConnection {

    private HashMap connectionMap = new HashMap();

    public static String propFile = "config/schema_diff.properties";

    private static DatabaseConnection _dbConn = null;
    private static Properties _config = null;


    public static DatabaseConnection getInstance() {
        if ( _dbConn == null ) {
	

	    _config = AppSupport.getProperties();
	    if ( _config == null ) {
		return null;
	    }

	    _dbConn = new DatabaseConnection(_config);
	}
        return _dbConn;
    }
    
    private DatabaseConnection(Properties config) {
        _config = config;
    }
    

    public Connection getConnection(String connName) {
	Connection _connection = (Connection) connectionMap.get(connName);
        if ( _connection != null ) {
	    return _connection;
	}
        
        try {
	    Properties connProps = new Properties();
	    String connDriverName = connName + ".driver";
	    String connDriverClass = _config.getProperty(connDriverName);
	    System.err.println(connDriverName + " = " + connDriverClass);

	    Driver driver
		= (Driver)Class.forName(connDriverClass).newInstance();
	    String connUser = connName + ".user";
	    String connUserName = _config.getProperty(connUser);
	    System.err.println(connUser + " = " + connUserName);
	    connProps.setProperty("user", connUserName);

	    String connPass = connName + ".password";
	    String connPassVal = _config.getProperty(connPass);
	    System.err.println(connPass + " = " + connPassVal);
	    connProps.setProperty("password", connPassVal);

	    String connUrl = connName + ".url";
	    String connUrlVal = _config.getProperty(connUrl);
	    System.err.println(connUrl + " = " + connUrlVal);
	    _connection = driver.connect(connUrlVal, connProps);
	    _connection.setAutoCommit(true);
            

	    connectionMap.put(connName, _connection);
	}
        catch ( Exception ex ) {
	    _connection = null;
	    ex.printStackTrace();
	}
        return _connection;
    }
    
    public DatabaseMetaData getDatabaseMetaData(String schemaName) 
	throws SQLException {
	DatabaseMetaData metaData = null;
	metaData = getConnection(schemaName).getMetaData();

	return metaData;
    }

    public void exec(String schemaName, String sql) throws SQLException {
        PreparedStatement ps
	    = getConnection(schemaName).prepareStatement(sql);
        ps.execute();
        close(ps,null);
    }
    
    public CallableStatement prepareCall(String schemaName,
					 String callStatement)
	throws SQLException {
	CallableStatement callStmt
	    = getConnection(schemaName).prepareCall(callStatement);

	return callStmt;

    }

    public PreparedStatement prepareStatement(String schemaName, 
					      String prepStatement)
	throws SQLException {
	PreparedStatement prepStmt
	    = getConnection(schemaName).prepareStatement(prepStatement);

	return prepStmt;

    }

    public long getId(String schemaName, String sql)
	throws SQLException
    {
        long id = 0;
        PreparedStatement ps = getConnection(schemaName).prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if ( rs.next() )
	    {
		id = rs.getLong(1);
	    }
        close(ps,rs);
        return id;
    }
    
    public ResultSet getResultSet(String schemaName, String sql)
	throws SQLException {
        long id = 0;
        PreparedStatement ps
	    = getConnection(schemaName).prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
	//	close(ps, null);
        return rs;
    }
    
    public int executeUpdate(String sql) 
	throws SQLException {
        long id = 0;
	int rc = 0;
	ResultSet rs = null;
        Statement stmt = getConnection(schemaName).createStatement();
        rc = stmt.executeUpdate(sql);

        return rc;
    }
    
    public void close(
		      PreparedStatement ps,
		      ResultSet rs) {
        if(rs!=null)
	    {
		try
		    {
			rs.close();
		    }
		catch(SQLException ex)
		    {
		    }
	    }
        if(ps!=null)
	    {
		try
		    {
			ps.close();
		    }
		catch(SQLException ex1)
		    {
		    }
	    }
    }
    
    public void closeAllConnections()
	throws SQLException
    {
	Set connKeys = connectionMap.keySet();
	Iterator connIter = connKeys.iterator();
	while (connIter.hasNext()) {
	    String connName = (String) connIter.next();
	    Connection _connection = (Connection) connectionMap.get(connName);
	    if ( _connection != null ) {
		_connection.close();
		_connection = null;
	    }
	}
    }
    

}


