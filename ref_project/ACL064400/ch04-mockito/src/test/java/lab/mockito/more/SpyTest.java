package lab.mockito.more;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
public class SpyTest {

	@Test
	public void when_spying_real_objects() throws Exception {
		Error error = new Error();
		error.setErrorCode("Q123");
		Error spyError = spy(error);
		// call real method from spy
		assertEquals("Q123", spyError.getErrorCode());

		// Changing value using spy
		spyError.setErrorCode(null);

		// verify spy has the changed value
		assertEquals(null, spyError.getErrorCode());

		// Stubbing method
		when(spyError.getErrorCode()).thenReturn("E456");

		// Changing value using spy
		spyError.setErrorCode(null);

		// Stubbed method value E456 is returned NOT NULL
		assertNotEquals(null, spyError.getErrorCode());

		// Stubbed method value E456
		assertEquals("E456", spyError.getErrorCode());

	}

	@Test
	public void show_mock_creation() {
		// Given
		List<String> mockedList = mock(ArrayList.class);
		// When
		mockedList.add("one");
		// Then
		verify(mockedList, times(1)).add("one");
		assertEquals(0, mockedList.size());
	}

	@Test
	public void show_spy_creation() {
		// Given
		List<String> spyList = spy(new ArrayList<>());
		// When
		spyList.add("one");
		// Then
		verify(spyList, times(1)).add("one");
		assertEquals(1, spyList.size());
	}

	@Test
	public void show_spy_partial_mock() throws Exception {
		// Given
		List<String> list = new ArrayList<>();
		List<String> spy = spy(list);
		when(spy.size()).thenReturn(100);
		// When
		spy.add("one");
		spy.add("two");
		// Then
		verify(spy).add("one");
		verify(spy).add("two");
		assertEquals("one", spy.get(0));
		assertEquals("two", spy.get(1));
		try {
			spy.get(2);
		} catch (Exception e) {
			assertTrue(e instanceof IndexOutOfBoundsException);
		}
		assertEquals(100, spy.size());
	}

	@Test
	public void Should_passed_When_doReturn() {
		// Given
		List<String> list = new ArrayList<>();
		List<String> spy = spy(list);
		doReturn("now reachable").when(spy).get(0);
		// When & Then
		assertEquals("now reachable", spy.get(0));
	}

	@Test
	public void Should_throws_exception_When_thenReturn() {
		// Given
		List<String> list = new ArrayList<>();
		List<String> spy = spy(list);
		assertThrows(IndexOutOfBoundsException.class, () -> {
			when(spy.get(0)).thenReturn("not reachable");
		});
	}

}
