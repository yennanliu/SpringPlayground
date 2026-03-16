package com.yen.FlinkRestService.model.enums;

public enum JobStatus {
    CREATED("CREATED"),
    RUNNING("RUNNING"),
    FAILING("FAILING"),
    FAILED("FAILED"),
    CANCELLING("CANCELLING"),
    CANCELED("CANCELED"),
    FINISHED("FINISHED"),
    RESTARTING("RESTARTING"),
    SUSPENDED("SUSPENDED"),
    RECONCILING("RECONCILING"),
    UNKNOWN("UNKNOWN");

    private final String value;

    JobStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static JobStatus fromValue(String value) {
        if (value == null) {
            return UNKNOWN;
        }
        for (JobStatus status : JobStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        return UNKNOWN;
    }
}
