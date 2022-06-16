package com.yen.web.service.impl;

import com.yen.service.myRedshiftService;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.redshiftdata.RedshiftDataClient;
import software.amazon.awssdk.services.redshiftdata.model.*;

import java.util.function.Consumer;

public class RedshiftServiceImpl implements myRedshiftService, RedshiftDataClient {
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

    @Override
    public String serviceName() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public BatchExecuteStatementResponse batchExecuteStatement(BatchExecuteStatementRequest batchExecuteStatementRequest) throws ValidationException, ActiveStatementsExceededException, BatchExecuteStatementException, AwsServiceException, SdkClientException, RedshiftDataException {
        return RedshiftDataClient.super.batchExecuteStatement(batchExecuteStatementRequest);
    }

    @Override
    public BatchExecuteStatementResponse batchExecuteStatement(Consumer<BatchExecuteStatementRequest.Builder> batchExecuteStatementRequest) throws ValidationException, ActiveStatementsExceededException, BatchExecuteStatementException, AwsServiceException, SdkClientException, RedshiftDataException {
        return RedshiftDataClient.super.batchExecuteStatement(batchExecuteStatementRequest);
    }

}
