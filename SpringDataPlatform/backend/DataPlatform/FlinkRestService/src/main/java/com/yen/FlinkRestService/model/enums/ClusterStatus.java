package com.yen.FlinkRestService.model.enums;

public enum ClusterStatus {
    ADDED("added"),
    UPDATED("updated"),
    CONNECTED("connected"),
    NOT_CONNECTED("not_connected"),
    UNKNOWN("unknown");

    private final String value;

    ClusterStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ClusterStatus fromValue(String value) {
        for (ClusterStatus status : ClusterStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        return UNKNOWN;
    }
}
