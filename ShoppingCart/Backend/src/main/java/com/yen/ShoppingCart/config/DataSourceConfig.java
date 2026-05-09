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
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Approach 5 — Read Replica routing.
 *
 * Enabled only when `app.datasource.replica.enabled=true` is set.
 * When disabled the default Spring Boot auto-configured DataSource is used unchanged.
 *
 * Routing rule:
 *   - @Transactional(readOnly = true)  → replica datasource
 *   - everything else                  → primary datasource
 */
@Configuration
@ConditionalOnProperty(name = "app.datasource.replica.enabled", havingValue = "true")
public class DataSourceConfig {

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
        targets.put("primary", primary);
        targets.put("replica", replica);
        routing.setTargetDataSources(targets);
        routing.setDefaultTargetDataSource(primary);
        return routing;
    }

    // Routes read-only transactions to the replica, writes to primary.
    static class RoutingDataSource extends AbstractRoutingDataSource {
        @Override
        protected Object determineCurrentLookupKey() {
            return TransactionSynchronizationManager.isCurrentTransactionReadOnly()
                    ? "replica"
                    : "primary";
        }
    }
}
