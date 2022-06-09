package lab.mockito.more;

import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(MockitoExtension.class)
public class ArgCaptorTest_Generic {

	@Mock
	private Service1 service1;

	@Test
	public void show_captures_collections_without_generic() {
		// When
		service1.call1(Arrays.asList("a", "b"));
		// Then
		ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
		verify(service1).call1(captor.capture());
		assertTrue(captor.getValue().containsAll(Arrays.asList("a", "b")));
	}
	
	@Test
	public void show_captures_collections_with_generic() {
		// When
		service1.call1(Arrays.asList("a", "b"));
		// Then
		Class<List<String>> listClass = (Class<List<String>>) (Class) List.class;
		ArgumentCaptor<List<String>> captor = ArgumentCaptor.forClass(listClass);
		verify(service1).call1(captor.capture());
		assertTrue(captor.getValue().containsAll(Arrays.asList("a", "b")));
	}

	@Captor
	ArgumentCaptor<List<String>> captor;
	@Test
	public void show_captures_collections_with_generic_and_annotation() {
		// When
		service1.call1(Arrays.asList("a", "b"));
		// Then
		verify(service1).call1(captor.capture());
		assertTrue(captor.getValue().containsAll(Arrays.asList("a", "b")));
	}
	
/*
	@Test
	public void Should_compile_failed_When_create_argument_captor_wrongly() {
		// When
		service1.call1(Arrays.asList("a", "b"));
		// Then
		ArgumentCaptor<List<String>> captor = ArgumentCaptor.forClass(List<String>.class);
		verify(service1).call1(captor.capture());
		assertTrue(captor.getValue().containsAll(Arrays.asList("a", "b")));
	}
*/


}
