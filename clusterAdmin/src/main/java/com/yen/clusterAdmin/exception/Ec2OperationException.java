package com.yen.clusterAdmin.exception;

public class Ec2OperationException extends RuntimeException {

    public Ec2OperationException(String message) {
        super(message);
    }

    public Ec2OperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
