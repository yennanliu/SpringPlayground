package lab.powermock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SuppressMethod.class)
public class SuppressMethodTest {

	@Test
	public void show_not_supresses_string_method() {
		SuppressMethod method = new SuppressMethod();
		assertTrue(method.format("10").contains("$"));
	}
	@Test
	public void show_supresses_string_method() {
		suppress(method(SuppressMethod.class, "getCurrency"));
		SuppressMethod method = new SuppressMethod();
		assertFalse(method.format("10").contains("$"));
	}

	@Test
	public void show_not_supresses_int_method() {
		SuppressMethod method = new SuppressMethod();
		assertEquals(20, method.plusNumber(10));
	}
	@Test
	public void show_supresses_int_method() {
		suppress(method(SuppressMethod.class, "getNumber"));
		SuppressMethod method = new SuppressMethod();
		assertEquals(10, method.plusNumber(10));
	}
}
