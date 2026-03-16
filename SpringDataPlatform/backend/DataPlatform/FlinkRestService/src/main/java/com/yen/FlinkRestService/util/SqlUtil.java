package com.yen.FlinkRestService.util;

/**
 * Utility class for SQL statement processing.
 */
public final class SqlUtil {

    private SqlUtil() {
        // Utility class - prevent instantiation
    }

    /**
     * Wraps SQL command in JSON format for Flink SQL Gateway API.
     * Example: "SELECT 1, 2, 3" -> {"statement": "SELECT 1, 2, 3"}
     */
    public static String prepareSqlCommand(String sqlCmd) {
        if (sqlCmd == null) {
            return null;
        }
        return "{\"statement\": \"" + sqlCmd + "\"}";
    }

    /**
     * Transforms multi-line SQL into a single line by normalizing whitespace.
     * Preserves string literals and handles SQL comments.
     *
     * Example:
     * Input:
     *   CREATE TABLE print_sink(
     *     a INT,
     *     b INT
     *   ) WITH (
     *     'connector' = 'print'
     *   )
     *
     * Output:
     *   CREATE TABLE print_sink( a INT, b INT ) WITH ( 'connector' = 'print' )
     */
    public static String transformToOneLine(String sqlCmd) {
        if (sqlCmd == null || sqlCmd.isEmpty()) {
            return sqlCmd;
        }

        // Remove single-line comments (-- comment)
        String result = sqlCmd.replaceAll("--[^\n]*", "");

        // Replace newlines and multiple whitespace with single space
        result = result.replaceAll("\\s+", " ");

        // Trim leading/trailing whitespace
        return result.trim();
    }

    /**
     * @deprecated Use {@link #prepareSqlCommand(String)} instead
     */
    @Deprecated
    public String prepareSQLcmd(String sqlCmd) {
        return prepareSqlCommand(sqlCmd);
    }

    /**
     * @deprecated Use {@link #transformToOneLine(String)} instead
     */
    @Deprecated
    public String transfromToOneLine(String sqlCmd) {
        return transformToOneLine(sqlCmd);
    }
}
