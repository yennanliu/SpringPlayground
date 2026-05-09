-- Runs on mysql-primary at first startup (docker-entrypoint-initdb.d).
-- Creates the replication user that mysql-replica will use for CHANGE MASTER TO.

CREATE USER IF NOT EXISTS 'repl'@'%' IDENTIFIED BY 'repl_password';
GRANT REPLICATION SLAVE ON *.* TO 'repl'@'%';
FLUSH PRIVILEGES;
