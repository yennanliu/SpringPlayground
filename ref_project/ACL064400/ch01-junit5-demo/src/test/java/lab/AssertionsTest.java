package lab;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class AssertionsTest {

	@Test
	void testAssertEquals() {
		// Test will pass
		assertEquals(4, Calculator.add(2, 2));

		// Test will fail
		assertEquals(3, Calculator.add(2, 2), "Jim: Calculator.add(2, 2) test failed");

		// Test will fail
		Supplier<String> messageSupplier = 
				() -> "Jim: Calculator.add(2, 2) test failed";
		assertEquals(3, Calculator.add(2, 2), messageSupplier);
	}

	@Test
	void testAssertNotEquals() {
		// Test will pass
		assertNotEquals(3, Calculator.add(2, 2));

		// Test will fail
		assertNotEquals(4, Calculator.add(2, 2), "Jim: Calculator.add(2, 2) test failed");

		// Test will fail
		Supplier<String> messageSupplier = 
				() -> "Jim: Calculator.add(2, 2) test failed";
		assertNotEquals(4, Calculator.add(2, 2), messageSupplier);
	}

	@Test
	void testAssertArrayEquals() {
		// Test will pass
		assertArrayEquals(new int[] { 1, 2, 3 }, new int[] { 1, 2, 3 });

		// Test will fail because element order is different
		assertArrayEquals(new int[] { 1, 2, 3 }, new int[] { 1, 3, 2 }, 
				"Jim: Array Equal Test");

		// Test will fail because number of elements are different
		assertArrayEquals(new int[] { 1, 2, 3 }, new int[] { 1, 2, 3, 4 }, 
				"Jim: Array Equal Test");
	}

	@Test
	void testAssertIterableEquals() {
		Iterable<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
		Iterable<Integer> list2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
		Iterable<Integer> list3 = new ArrayList<>(Arrays.asList(1, 2, 3));
		Iterable<Integer> list4 = new ArrayList<>(Arrays.asList(1, 2, 4, 3));

		// Test will pass
		assertIterableEquals(list1, list2);

		// Test will fail
		assertIterableEquals(list1, list3);

		// Test will fail
		assertIterableEquals(list1, list4);
	}

	@Test
	void testAssertNullAndNotNull() {
		String nullString = null;
		String notNullString = "Hi";

		// Test will pass
		assertNotNull(notNullString);

		// Test will fail
		assertNotNull(nullString);

		// Test will pass
		assertNull(nullString);

		// Test will fail
		assertNull(notNullString);
	}

	@Test
	void testAssertSameAndNotSame() {
		String originalObject = "Hi";
		String cloneObject = originalObject;
		String otherObject = "Hello";

		// Test will pass
		assertNotSame(originalObject, otherObject);

		// Test will fail
		assertNotSame(originalObject, cloneObject);

		// Test will pass
		assertSame(originalObject, cloneObject);

		// Test will fail
		assertSame(originalObject, otherObject);
	}

	@Test
	void testAssertTimeout() {
		// This will pass
		assertTimeout(Duration.ofMinutes(1), () -> {
			return "result";
		});

		// This will fail
		assertTimeout(Duration.ofSeconds(10), () -> {
			Thread.sleep(20 * 1000);
			return "result";
		});

		// This will fail
		assertTimeoutPreemptively(Duration.ofSeconds(10), () -> {
			Thread.sleep(20 * 1000);
			return "result";
		});
	}

	@Test
	void testAssertTrueFalse() {

		boolean isTrue = true;
		boolean isFalse = false;

		assertTrue(isTrue);
		assertTrue(isFalse, "Jim: test execution message");
		assertTrue(isFalse, AssertionsTest::message);
		assertTrue(AssertionsTest::getResult, AssertionsTest::message);

		assertFalse(isFalse);
		assertFalse(isTrue, "Jim: test execution message");
		assertFalse(isTrue, AssertionsTest::message);
		assertFalse(AssertionsTest::getResult, AssertionsTest::message);
	}

	private static String message() {
		return "Test execution result";
	}

	private static boolean getResult() {
		return true;
	}

	@Test
	void testAssertThrows() {
		assertThrows(IllegalArgumentException.class, () -> {
			Integer.parseInt("Not a integer");
		});
	}

	@Test
	void testFail() {

	    fail("the execution is not expected to this line!");
		fail(AssertionsTest::message);
	}

}
