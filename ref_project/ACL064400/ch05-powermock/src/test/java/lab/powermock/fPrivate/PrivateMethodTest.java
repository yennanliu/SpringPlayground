package lab.powermock.fPrivate;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PrivateMethod.class)
public class PrivateMethodTest {

	@Test
	public void show_stubs_private_methods() throws Exception {
		PrivateMethod privateMethodSpy = spy(new PrivateMethod());
		when(privateMethodSpy, "secretValue").thenReturn("123");

		assertEquals("123", privateMethodSpy.exposeSecretValue());
	}
}
