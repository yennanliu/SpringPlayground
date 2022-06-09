package lab.mockito.more;

import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShowMockitoBehavior {

	class KlassHasVoidMethod {
		void aVoidMethod() {
			System.out.println("real void method called");
		}
	}

	@Test
	public void Should_do_nothing_When_given_void_method_with_doNothing() {
		// Given
		KlassHasVoidMethod klass = mock(KlassHasVoidMethod.class);
		doNothing().when(klass).aVoidMethod();
		// When
		klass.aVoidMethod();
		// Then
		verify(klass).aVoidMethod();
	}

	@Test
	public void Should_do_nothing_When_given_void_method_without_doNothing() {
		// Given
		KlassHasVoidMethod klass = mock(KlassHasVoidMethod.class);
		// When
		klass.aVoidMethod();
		// Then
		verify(klass).aVoidMethod();
	}
	
	@Test
	public void Should_do_chain_call_When_multiple_assumption() {
		KlassHasVoidMethod some = mock(KlassHasVoidMethod.class);
		doThrow(new RuntimeException("call doThrow()")).	// 1
		doNothing().	// 2
		doCallRealMethod().	// 3
		doAnswer(new Answer<Void>() {	// 4,5....
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				System.out.println("call doAnswer()");
				return null;
			}
		}).when(some).aVoidMethod();
		try {
			some.aVoidMethod();	//1:doThrow()
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		some.aVoidMethod();	//2:doNothing()
		some.aVoidMethod();	//3:doCallRealMethod()
		some.aVoidMethod();	//4:doAnswer()
		some.aVoidMethod();	//5:doAnswer()
	}
	
	@Test
	public void Should_do_real_When_call_void_method() {
		KlassHasVoidMethod some = mock(KlassHasVoidMethod.class);
		doCallRealMethod().when(some).aVoidMethod();
		some.aVoidMethod();
	}

	class KlassHasReturnMethod {
		String aReturnMethod() {
			return "real return method called";
		}
	}
	
	@Test
	public void Should_test_error_When_do_return_is_not_safe() {
		// Given1
		KlassHasReturnMethod some1 = mock(KlassHasReturnMethod.class);
		when(some1.aReturnMethod()).thenReturn("aReturnMethod is called");
		// When & Then
		assertEquals("aReturnMethod is called", some1.aReturnMethod());

		// Given2
		KlassHasReturnMethod some2 = mock(KlassHasReturnMethod.class);
		doReturn(1.111d).when(some2.aReturnMethod());
	}

	@Test
	public void Should_return_null_When_call_return_method() {
		KlassHasReturnMethod some = mock(KlassHasReturnMethod.class);
		System.out.println(some.aReturnMethod());
	}

}
