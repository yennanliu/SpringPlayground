package com.yen.ShoppingCart.config;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Approach 5 — Read Replica routing.
 *
 * Enabled only when `app.datasource.replica.enabled=true` is set.
 * When disabled the default Spring Boot auto-configured DataSource is used unchanged.
 *
 * Routing rule:
 *   @Transactional(readOnly = true)  → REPLICA
 *   all other transactions           → PRIMARY
 *
 * LazyConnectionDataSourceProxy is required:
 *   Without it, Spring's transaction manager calls DataSource.getConnection() eagerly —
 *   before @Transactional sets the read-only flag on TransactionSynchronizationManager.
 *   LazyConnectionDataSourceProxy defers actual connection acquisition until the first
 *   SQL statement, by which point the read-only flag is already set, so routing works correctly.
 */
@Configuration
@ConditionalOnProperty(name = "app.datasource.replica.enabled", havingValue = "true")
public class DataSourceConfig {

    // Constants prevent typo bugs if routing keys are referenced elsewhere
    static final String KEY_PRIMARY = "primary";
    static final String KEY_REPLICA = "replica";

    @Bean("primaryDataSource")
    @ConfigurationProperties(prefix = "app.datasource.primary")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean("replicaDataSource")
    @ConfigurationProperties(prefix = "app.datasource.replica")
    public DataSource replicaDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean
    public DataSource dataSource(
            @Qualifier("primaryDataSource") DataSource primary,
            @Qualifier("replicaDataSource") DataSource replica) {

        RoutingDataSource routing = new RoutingDataSource();

        Map<Object, Object> targets = new HashMap<>();
        targets.put(KEY_PRIMARY, primary);
        targets.put(KEY_REPLICA, replica);
        routing.setTargetDataSources(targets);
        routing.setDefaultTargetDataSource(primary);
        routing.afterPropertiesSet();

        // Wrap in LazyConnectionDataSourceProxy so the routing decision is deferred
        // until the first SQL statement — at which point @Transactional has already set
        // the read-only flag and routing picks the correct datasource.
        return new LazyConnectionDataSourceProxy(routing);
    }

    // Routes read-only transactions to the replica, all writes to the primary.
    static class RoutingDataSource extends AbstractRoutingDataSource {
        @Override
        protected Object determineCurrentLookupKey() {
            return TransactionSynchronizationManager.isCurrentTransactionReadOnly()
                    ? KEY_REPLICA
                    : KEY_PRIMARY;
        }
    }
}
