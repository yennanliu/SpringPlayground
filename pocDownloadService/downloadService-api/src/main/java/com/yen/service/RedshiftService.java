package com.yen.service;

import org.springframework.stereotype.Service;

@Service
public interface RedshiftService {

    public String runQuery(String querySQL);

    public String runUnload(String unloadSQL);
}
