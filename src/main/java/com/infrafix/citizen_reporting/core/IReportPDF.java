package com.infrafix.citizen_reporting.core;

import jakarta.servlet.http.HttpServletResponse;

public interface IReportPDF {
    void downloadReportPDF (Long reportId, HttpServletResponse response);
}
