package com.yen.web.service.impl;

import com.yen.service.RedshiftService;

public class RedshiftServiceImpl implements RedshiftService {
    @Override
    public String runQuery(String querySQL) {
        System.out.println("run query : " + querySQL);
        return null;
    }

    @Override
    public String runUnload(String srcTable, String destFile) {
        String unloadCmd = String.format("unload ('select * from %s') to 's3://%s' iam_role 'arn:aws:iam::173062506398:role/service-role/AmazonRedshift-CommandsAccessRole-20220531T194730'parallel off;", srcTable, destFile);
        return unloadCmd;
    }

    public void runQuery() {
        System.out.println("run query >>>>> " );
    }

}
