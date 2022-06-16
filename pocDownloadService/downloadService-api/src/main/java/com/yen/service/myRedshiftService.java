package com.yen.service;

import org.springframework.stereotype.Service;

@Service
public interface myRedshiftService {

    public String runQuery(String querySQL);

    public String runUnload(String srcTable, String destFile);
}
