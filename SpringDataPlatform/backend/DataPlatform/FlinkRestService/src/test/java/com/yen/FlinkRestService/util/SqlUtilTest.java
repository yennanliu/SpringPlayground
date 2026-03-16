package com.yen.FlinkRestService.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SqlUtilTest {

    @Test
    void testPrepareSqlCommand() {
        String sql = "SELECT 1, 2, 3";
        String result = SqlUtil.prepareSqlCommand(sql);
        assertEquals("{\"statement\": \"SELECT 1, 2, 3\"}", result);
    }

    @Test
    void testPrepareSqlCommand_Null() {
        assertNull(SqlUtil.prepareSqlCommand(null));
    }

    @Test
    void testTransformToOneLine_MultilineSQL() {
        String multilineSql = """
                CREATE TABLE print_sink(
                    a INT,
                    b INT
                ) WITH (
                    'connector' = 'print'
                )
                """;

        String result = SqlUtil.transformToOneLine(multilineSql);

        assertEquals("CREATE TABLE print_sink( a INT, b INT ) WITH ( 'connector' = 'print' )", result);
    }

    @Test
    void testTransformToOneLine_WithComments() {
        String sqlWithComments = """
                SELECT * FROM users -- get all users
                WHERE active = true
                """;

        String result = SqlUtil.transformToOneLine(sqlWithComments);

        assertEquals("SELECT * FROM users WHERE active = true", result);
    }

    @Test
    void testTransformToOneLine_SingleLine() {
        String singleLine = "SELECT 1, 2, 3";
        String result = SqlUtil.transformToOneLine(singleLine);
        assertEquals("SELECT 1, 2, 3", result);
    }

    @Test
    void testTransformToOneLine_Null() {
        assertNull(SqlUtil.transformToOneLine(null));
    }

    @Test
    void testTransformToOneLine_Empty() {
        assertEquals("", SqlUtil.transformToOneLine(""));
    }

    @Test
    void testTransformToOneLine_ExtraWhitespace() {
        String sql = "SELECT   a,    b   FROM   table1";
        String result = SqlUtil.transformToOneLine(sql);
        assertEquals("SELECT a, b FROM table1", result);
    }
}
