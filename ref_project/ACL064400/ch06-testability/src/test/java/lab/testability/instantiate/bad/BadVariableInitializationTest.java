package lab.testability.instantiate.bad;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class BadVariableInitializationTest {
	@Test
	public void show_object_is_not_null() throws Exception {
		BadVariableInitialization instance= new BadVariableInitialization();
		assertNotNull(instance);
		assertEquals("Jim", instance.someMethod("Jim"));
	}
}
