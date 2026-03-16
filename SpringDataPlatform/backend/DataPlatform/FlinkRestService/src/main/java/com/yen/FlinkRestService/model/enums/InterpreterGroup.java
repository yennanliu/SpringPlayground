package com.yen.FlinkRestService.model.enums;

/**
 * Zeppelin interpreter groups.
 * @see <a href="https://zeppelin.apache.org/docs/latest/usage/interpreter/overview.html">Zeppelin Interpreters</a>
 */
public enum InterpreterGroup {
    FLINK("flink"),
    SPARK("spark"),
    PYTHON("python"),
    JDBC("jdbc"),
    SHELL("sh"),
    MARKDOWN("md");

    private final String value;

    InterpreterGroup(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static InterpreterGroup fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (InterpreterGroup group : values()) {
            if (group.value.equalsIgnoreCase(value)) {
                return group;
            }
        }
        throw new IllegalArgumentException("Unknown interpreter group: " + value);
    }
}
