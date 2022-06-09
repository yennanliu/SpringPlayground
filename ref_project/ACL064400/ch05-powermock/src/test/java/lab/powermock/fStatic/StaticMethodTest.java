package lab.powermock.fStatic;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = StaticMethodDemo.class)
public class StaticMethodTest {

	@Test
	public void show_stubs_static_method() throws Exception {
		System.out.println(StaticMethodDemo.staticGenerateId());

		mockStatic(StaticMethodDemo.class);
	//	when(StaticMethodDemo.staticGenerateId()).thenReturn(1);
		// org.mockito.exceptions.misusing.NotAMockException: Argument should be a mock, but is: class java.lang.Class
		when(StaticMethodDemo.staticGenerateId())
			.thenAnswer((Answer<Integer>) (invocation -> 1));
		assertEquals(1, StaticMethodDemo.staticGenerateId());
	}
}
