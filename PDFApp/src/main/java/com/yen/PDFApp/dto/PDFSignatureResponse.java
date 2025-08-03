package com.yen.PDFApp.dto;

public class PDFSignatureResponse {
    private String fileName;
    private String message;
    private boolean success;

    public PDFSignatureResponse() {
    }

    public PDFSignatureResponse(String fileName, String message, boolean success) {
        this.fileName = fileName;
        this.message = message;
        this.success = success;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}