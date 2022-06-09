package lab.testability.staticmethods.bad;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StaticMethodTest {
	@Mock
	StaticMethodDemo staticMethodDemo;
	
	@Test
	public void show_mock_static_method() throws Exception {
		// Given
		Mockito.doNothing().when(staticMethodDemo).aStaticMethod();
		// When
		staticMethodDemo.aStaticMethod();
	}
}

