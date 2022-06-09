package lab.powermock.fStatic;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("lab.powermock.fStatic.StaticInitDemo")
public class StaticInitTest {

	@Test
	public void show_supresses_static_initialization() {

		assertEquals(0, StaticInitDemo.value);
		assertEquals(null, StaticInitDemo.MAP);

		Map<String, String> m = new HashMap<>();
		m.put("a", "jim");
		Whitebox.setInternalState(StaticInitDemo.class, m);

		int i = 111;
		Whitebox.setInternalState(StaticInitDemo.class, i);

		assertEquals(111, StaticInitDemo.value);
		assertEquals("jim", StaticInitDemo.MAP.get("a"));
	}
}
