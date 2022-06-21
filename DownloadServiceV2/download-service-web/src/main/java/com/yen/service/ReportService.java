package com.yen.service;

import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.List;

@Service
public class ReportService {

    public String[] getReportField(String reportName){

        String[] baseCols = new String[]{};

        String[] tradeCols = new String[]{"date", "trade_id", "amount"};
        String[] depositCols = new String[]{"date", "deposit_id", "amount"};
        String[] withdrawCols = new String[]{"date", "withdraw_id", "amount"};

        if (reportName.toLowerCase().equals("trade")){
            return tradeCols;
        } else if (reportName.toLowerCase().equals("deposit")) {
            return depositCols;
        } else if (reportName.toLowerCase().equals("withdraw")) {
            return withdrawCols;
        }

        System.out.println(">>> invalid reportName : " + reportName);
        return baseCols;
    }

}
