package com.infrafix.citizen_reporting.util;


import com.infrafix.citizen_reporting.config.OtherConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggingFile {
    private static StringBuilder sBuild = new StringBuilder();
    private static Logger logger = LogManager.getLogger(LoggingFile.class);

    public static void logException(String strClass, String strMethod, Exception ex){
        if(OtherConfig.getEnableLogFile().equals("y")){
            sBuild.setLength(0);
            logger.error(sBuild.append(System.getProperty("line.separator")).
                    append("ERROR IN CLASS ==> ").append(strClass).append(System.getProperty("line.separator")).
                    append("METHOD ==> ").append(strMethod).append(System.getProperty("line.separator")).
                    append("ERROR CAUSE ==> ").append(ex.getCause()).append(System.getProperty("line.separator")).
                    append("ERROR IS ==> ").append(ex.getMessage()).append(System.getProperty("line.separator")));
        }
    }
}
