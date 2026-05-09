#!/bin/bash
# Runs inside the mysql-replica container via docker-entrypoint-initdb.d.
# Waits for mysql-primary to be reachable, then configures GTID-based replication.
# This script runs after this container's own MySQL instance is up (entrypoint guarantee).

set -e

echo "[replica-init] Waiting for mysql-primary to be ready..."
until mysql -hmysql-primary -uroot --connect-timeout=3 -e "SELECT 1" >/dev/null 2>&1; do
  echo "[replica-init] Primary not ready yet, retrying in 3s..."
  sleep 3
done
echo "[replica-init] Primary is ready."

mysql -uroot <<-SQL
  CHANGE MASTER TO
    MASTER_HOST      = 'mysql-primary',
    MASTER_USER      = 'repl',
    MASTER_PASSWORD  = 'repl_password',
    MASTER_AUTO_POSITION = 1;
  START SLAVE;
SQL

echo "[replica-init] Replication started. Run SHOW SLAVE STATUS\\G to verify."
