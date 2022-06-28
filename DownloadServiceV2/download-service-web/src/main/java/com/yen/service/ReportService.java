package com.yen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface ReportService {

    public String[] getReportField(String reportName);
}
