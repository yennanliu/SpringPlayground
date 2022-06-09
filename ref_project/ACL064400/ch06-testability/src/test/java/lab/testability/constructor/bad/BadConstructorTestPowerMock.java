package lab.testability.constructor.bad;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

public class BadConstructorTestPowerMock {
	@Test
	public void show_object_is_not_null() throws Exception {
		BadConstructor instance 
				= Whitebox.newInstance(BadConstructor.class);
		assertNotNull(instance);
		assertEquals("Jim", instance.someMethod("Jim"));
	}
}
