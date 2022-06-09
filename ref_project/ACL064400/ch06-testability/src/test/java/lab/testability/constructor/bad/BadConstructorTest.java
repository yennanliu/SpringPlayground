package lab.testability.constructor.bad;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BadConstructorTest {

	BadConstructor instance;

	@Before
	public void setUp() {
		instance = new BadConstructor();
	}

	@Test
	public void show_SUT_is_not_null() throws Exception {
		assertNotNull(instance);
		assertEquals("Jim", instance.someMethod("Jim"));
	}
}
