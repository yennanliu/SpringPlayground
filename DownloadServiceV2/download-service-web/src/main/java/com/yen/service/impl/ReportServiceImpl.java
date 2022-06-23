package com.yen.service.impl;

import com.yen.service.ReportService;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {

    @Override
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
