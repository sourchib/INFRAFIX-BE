package com.infrafix.citizen_reporting.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:otherconfig.properties")
public class OtherConfig {
    private static String enableLogFile;
    private static String enablePrintConsole;
    private static Integer defaultPaginationSize;

    // Log File
    public static String getEnableLogFile() {
        return enableLogFile;
    }
    @Value("${enable.log.file}")
    private void setEnableLogFile(String enableLogFile){
        OtherConfig.enableLogFile = enableLogFile;
    }


    // Print Console
    public static String getEnablePrintConsole() {
        return enablePrintConsole;
    }
    @Value("${enable.print.console}")
    private void setEnablePrintConsole(String enablePrintConsole){
        OtherConfig.enablePrintConsole = enablePrintConsole;
    }

    // Default Pagination
    public static Integer getDefaultPaginationSize() {
        return defaultPaginationSize;
    }
    @Value("${default.pagination.size}")
    private void setDefaultPaginationSize(Integer defaultPaginationSize){
        OtherConfig.defaultPaginationSize = defaultPaginationSize;
    }
}
