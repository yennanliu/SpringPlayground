package EmployeeSystem.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HelperTest {

    @Test
    public void testNotNullWithNonNullObject() {
        Object obj = new Object();
        assertTrue(Helper.notNull(obj));
    }

    @Test
    public void testNotNullWithNullObject() {
        Object obj = null;
        assertFalse(Helper.notNull(obj));
    }

}
