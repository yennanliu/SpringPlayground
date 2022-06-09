package lab.testability.staticmethods.good;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import lab.testability.TestingImpedimentException;

@RunWith(MockitoJUnitRunner.class)
public class StaticMethodDelegateTest {
	@Mock
	StaticMethodDelegate staticMethodDelegate;
	@Test
	public void show_mock_static_method() {
		// Given 
		Mockito.doNothing().when(staticMethodDelegate).delegate();
		try {
			// When
			staticMethodDelegate.delegate();
		} catch (TestingImpedimentException e) {
			// Then
			fail();
		}
	}
}
