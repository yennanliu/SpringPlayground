package lab.powermock.fConstructor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

public class SuppressConstructorTest {

	@Test
	public void show_supresses_self_constructor() throws Exception {
		SuppressConstructor instance = Whitebox.newInstance(SuppressConstructor.class);
		assertNotNull(instance);
		assertEquals(0, instance.getSomeInt());
		assertEquals(false, instance.isSomeBoolean());
	}
}
