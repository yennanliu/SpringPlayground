package com.yen.clusterAdmin.model.dto;

public class CommandResultDTO {

    private String commandId;
    private String status;
    private String output;
    private String error;
    private Integer responseCode;
    private String message;

    public CommandResultDTO() {}

    public CommandResultDTO(String commandId, String message) {
        this.commandId = commandId;
        this.message = message;
    }

    public CommandResultDTO(String commandId, String status, String output, String error, Integer responseCode) {
        this.commandId = commandId;
        this.status = status;
        this.output = output;
        this.error = error;
        this.responseCode = responseCode;
    }

    // Getters and Setters
    public String getCommandId() { return commandId; }
    public void setCommandId(String commandId) { this.commandId = commandId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getOutput() { return output; }
    public void setOutput(String output) { this.output = output; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public Integer getResponseCode() { return responseCode; }
    public void setResponseCode(Integer responseCode) { this.responseCode = responseCode; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
