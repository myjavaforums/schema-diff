/**
 * $Id$
 *
 * Copyright 2008 - Seth M. Fuller
 * 
 * License: Apache 2.0
 *
 */
package org.schema_diff;
/**
 * This class consists of static methods to support the application
 *
 */

import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;

import org.schema_diff.utils.DateUtilities;

public class AppSupport {
    public static String propFile = "config/schema_diff.properties";

    private static Properties _config = null;
    public static boolean isDebug = false;
    public static int verboseLevel = 2;

    public static Properties getProperties() {
	
	if (null == _config) {
	    try {
		InputStream inProp
		    = AppSupport.class.getClassLoader().getResourceAsStream(propFile);
		if (inProp == null) {
		    System.err.println("Properties file not found:"
				       + propFile);
		    return null;
		}

		_config = new java.util.Properties();
		if ( _config != null ) {
		    _config.load(inProp);
		}
	    }
	    catch (IOException exc) {
		System.err.println("Can't read " + propFile);
		exc.printStackTrace();
		return null;
	    }
	}
        return _config;
    }

    public static String getProperty(String propName) {
	String propVal = null;

	propVal = AppSupport.getProperties().getProperty(propName);

	return propVal;
    }

    public static void debugMessage(String dbgMsg) {
	if (isDebug) {
	    System.err.println(dbgMsg + " AT: " + DateUtilities.now());
	}
    }

    public static void infoMessage(int verboseLvl, String infoMsg) {
	if (verboseLvl >= verboseLevel) {
	    System.out.println(infoMsg
			       + " AT: " + DateUtilities.now());
	}
    }

    public static long usedMemory ()
    {
        return s_runtime.totalMemory () - s_runtime.freeMemory ();
    }
    
    private static final Runtime s_runtime = Runtime.getRuntime ();

}
