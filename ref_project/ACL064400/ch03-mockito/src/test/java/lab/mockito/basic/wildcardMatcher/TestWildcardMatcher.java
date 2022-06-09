package lab.mockito.basic.wildcardMatcher;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TestWildcardMatcher {
	@Test
	public void Should_not_get_output_When_not_use_wildcard_matcher() {
		// Given
		Service mockService = mock(Service.class);
		Object mockInput = mock(Object.class);

		Request req = new Request(mockInput);
		Response resp = new Response("test");
		when(mockService.call(req)).thenReturn(resp);
		
		// When
		ServiceFacade j = new ServiceFacade(mockService);
		Object output = j.call(mockInput);
		
		// Then
		assertEquals("test", output.toString());
	}

	@Test
	public void Should_get_output_When_use_wildcard_matcher() {
		// Given
		Service mockService = mock(Service.class);
		Object mockInput = mock(Object.class);

		Request req = isA(Request.class);
		Response resp = new Response("test");
		when(mockService.call(req)).thenReturn(resp);
		
		// When
		ServiceFacade j = new ServiceFacade(mockService);
		Object output = j.call(mockInput);
		
		// Then
		assertEquals("test", output.toString());
	}
}
