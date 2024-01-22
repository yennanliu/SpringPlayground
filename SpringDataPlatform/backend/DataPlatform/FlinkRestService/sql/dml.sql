
-- cmd clean DB data

truncate job;
truncate job_jar;
truncate notebook;
truncate cluster;

-- truncate option_schema;
Spark SQL, Hive, JDBC, Markdown, Shell and so on.

INSERT INTO `option_schema` (id, active, column_name, schema_name)
VALUES
(1001, 0, "test_col", "test_schema"),
(1002, 1, "test_col_2", "test_schema_2"),
(2001, 1, "flink", "interpreter"),
(2002, 1, "spark", "interpreter"),
(2003, 1, "sql", "interpreter"),
(2004, 1, "python", "interpreter"),
(2005, 1, "hive", "interpreter");
