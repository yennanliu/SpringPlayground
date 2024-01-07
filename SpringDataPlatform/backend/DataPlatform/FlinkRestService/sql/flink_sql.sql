-- https://support.huaweicloud.com/sqlref-flink-dli/dli_08_0345.html

--create table printSink (
--  attr_name attr_type (',' attr_name attr_type) * (',' PRIMARY KEY (attr_name,...) NOT ENFORCED)
--) with (
--  'connector' = 'print',
--  'print-identifier' = '',
--  'standard-error' = ''
--);


CREATE table print_sink(
    a int,
    b int
) WITH (
    'connector' = 'print',
    'print-identifier' = '',
)

INSERT INTO print_sink
SELECT
1, 2