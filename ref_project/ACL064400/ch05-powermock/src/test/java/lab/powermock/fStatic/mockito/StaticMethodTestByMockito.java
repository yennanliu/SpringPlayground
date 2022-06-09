package lab.powermock.fStatic.mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import lab.powermock.fStatic.StaticMethodDemo;

@RunWith(MockitoJUnitRunner.class)
public class StaticMethodTestByMockito {

	@Mock
	StaticMethodDemo staticMethodDemo;

	@Test
	public void show_stubs_static_method() throws Exception {

		when(staticMethodDemo.generateId()).thenReturn(1);
		assertEquals(1, staticMethodDemo.generateId());

		when(StaticMethodDemo.staticGenerateId()).thenReturn(2);
		assertEquals(2, staticMethodDemo.staticGenerateId());
	}
	
	@Test
	public void show_stubs_static_method_with_mockStatic() {
		try (MockedStatic<StaticMethodDemo> mock = Mockito.mockStatic(StaticMethodDemo.class)) {
			mock.when(StaticMethodDemo::staticGenerateId).thenReturn(2);
			assertEquals(2, StaticMethodDemo.staticGenerateId());
		}
		assertNotEquals(2, StaticMethodDemo.staticGenerateId());
	}

}
