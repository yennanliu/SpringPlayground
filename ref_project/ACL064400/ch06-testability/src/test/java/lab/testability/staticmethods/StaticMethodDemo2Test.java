package lab.testability.staticmethods;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;

import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class StaticMethodDemo2Test {
	
	@Test
	public void show_not_mock_static_void_method() {
		try {
			StaticMethodDemo2.aVoid();
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof RuntimeException);
		}
	}

	@Test
	public void show_mock_static_void_method_by_default() {
		// Given
		try (MockedStatic<StaticMethodDemo2> mock = Mockito.mockStatic(StaticMethodDemo2.class)) {
			// When
			StaticMethodDemo2.aVoid();
			// Then
			mock.verify(() -> StaticMethodDemo2.aVoid(), times(1));
		}
	}
	
	@Test
	public void show_mock_static_void_method_by_answer() {
		// Given
		try (MockedStatic<StaticMethodDemo2> mock = Mockito.mockStatic(StaticMethodDemo2.class)) {
			mock.when(StaticMethodDemo2::aVoid).then(new Answer<Void>() {
				@Override
				public Void answer(InvocationOnMock invocation) throws Throwable {
					return null;
				}
			});
			// When
			StaticMethodDemo2.aVoid();
			// Then
			mock.verify(() -> StaticMethodDemo2.aVoid(), times(1));
		}
	}

	@Test
	public void show_mock_static_void_method_by_answer_lambda() {
		// Given
		try (MockedStatic<StaticMethodDemo2> mock = Mockito.mockStatic(StaticMethodDemo2.class)) {
			mock.when(StaticMethodDemo2::aVoid).then(invocation -> null);
			// When
			StaticMethodDemo2.aVoid();
			// Then
			mock.verify(() -> StaticMethodDemo2.aVoid(), times(1));
		}
	}
	
	@Test
	public void show_mock_static_return_method() {
		assertEquals("Hello", StaticMethodDemo2.aReturn());
		
		try (MockedStatic<StaticMethodDemo2> mock = Mockito.mockStatic(StaticMethodDemo2.class)) {
			mock.when(StaticMethodDemo2::aReturn).thenReturn("method is mocked!");
			
			assertEquals("method is mocked!", StaticMethodDemo2.aReturn());
		}
		
		assertEquals("Hello", StaticMethodDemo2.aReturn());
	}

	@Test
	public void show_mock_static_return_method_with_params() {
		assertEquals("Hello Jim", StaticMethodDemo2.aParamReturn("Jim"));
		
		try (MockedStatic<StaticMethodDemo2> mock = Mockito.mockStatic(StaticMethodDemo2.class)) {
			mock.when(() -> StaticMethodDemo2.aParamReturn("Jim")).thenReturn("method is mocked!");
			
			assertEquals("method is mocked!", StaticMethodDemo2.aParamReturn("Jim"));
		}
		
		assertEquals("Hello Jim", StaticMethodDemo2.aParamReturn("Jim"));
	}

}
