package com.yen.ShoppingCart.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Unit tests for DataSourceConfig.RoutingDataSource.
 *
 * Core invariant (Approach 5):
 *   isCurrentTransactionReadOnly() == true  → "replica"
 *   isCurrentTransactionReadOnly() == false → "primary"
 *
 * We test the routing logic directly by manipulating
 * TransactionSynchronizationManager's read-only flag —
 * no database or Spring context needed.
 */
class DataSourceRoutingTest {

    // Instantiate the package-private inner class via reflection
    private Object routingDataSource;
    private Method determineKey;

    @BeforeEach
    void setUp() throws Exception {
        // Locate the inner class
        Class<?> routingClass = null;
        for (Class<?> c : DataSourceConfig.class.getDeclaredClasses()) {
            if (c.getSimpleName().equals("RoutingDataSource")) {
                routingClass = c;
                break;
            }
        }
        assertNotNull(routingClass, "RoutingDataSource inner class must exist");

        routingDataSource = routingClass.getDeclaredConstructor().newInstance();
        determineKey = routingClass.getDeclaredMethod("determineCurrentLookupKey");
        determineKey.setAccessible(true);

        // initialize the synchronization manager so the flag is queryable
        TransactionSynchronizationManager.initSynchronization();
    }

    @AfterEach
    void tearDown() {
        TransactionSynchronizationManager.clearSynchronization();
        TransactionSynchronizationManager.setCurrentTransactionReadOnly(false);
    }

    @Test
    void readOnlyTransaction_shouldRouteToReplica() throws Exception {
        TransactionSynchronizationManager.setCurrentTransactionReadOnly(true);

        Object key = determineKey.invoke(routingDataSource);

        assertEquals("replica", key, "Read-only transaction must route to replica");
    }

    @Test
    void writeTransaction_shouldRouteToPrimary() throws Exception {
        TransactionSynchronizationManager.setCurrentTransactionReadOnly(false);

        Object key = determineKey.invoke(routingDataSource);

        assertEquals("primary", key, "Write transaction must route to primary");
    }

    @Test
    void defaultTransaction_shouldRouteToPrimary() throws Exception {
        // no explicit read-only flag set → defaults to false
        Object key = determineKey.invoke(routingDataSource);

        assertEquals("primary", key, "Default (no flag) must route to primary");
    }

    @Test
    void toggleBetweenReadAndWrite_shouldSwitchCorrectly() throws Exception {
        TransactionSynchronizationManager.setCurrentTransactionReadOnly(true);
        assertEquals("replica", determineKey.invoke(routingDataSource));

        TransactionSynchronizationManager.setCurrentTransactionReadOnly(false);
        assertEquals("primary", determineKey.invoke(routingDataSource));

        TransactionSynchronizationManager.setCurrentTransactionReadOnly(true);
        assertEquals("replica", determineKey.invoke(routingDataSource));
    }
}
