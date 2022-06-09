package lab.mockito.more;

import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("unlikely-arg-type")
@ExtendWith(MockitoExtension.class)
public class ArgCaptorTest_Varargs_Array {

	@Mock
	private IPassedIn iPassedIn;

	@Test
	public void show_capture_variable_args_by_array() {
		// Given
		String[] arr = { "a", "b", "c" };
		// When
		iPassedIn.passedInVarargs(arr);
		// Then
		ArgumentCaptor<String[]> captor = ArgumentCaptor.forClass(String[].class);
		verify(iPassedIn).passedInVarargs(captor.capture());
		assertTrue(captor.getAllValues().containsAll(Arrays.asList(arr)));
	}

	@Test
	public void show_capture_variable_args_by_multiple_capture() {
		// Given
		String[] arr = { "a", "b", "c" };
		// When
		iPassedIn.passedInVarargs(arr);
		// Then
		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(iPassedIn).passedInVarargs(captor.capture(), captor.capture(), captor.capture());
		assertTrue(captor.getAllValues().containsAll(Arrays.asList(arr)));
	}

	@Test
	public void show_capture_array() {
		// Given
		String[] arr = { "a", "b", "c" };
		// When
		iPassedIn.passedInArray(arr);
		// Then
		ArgumentCaptor<String[]> captor = ArgumentCaptor.forClass(String[].class);
		verify(iPassedIn).passedInArray(captor.capture());
		assertTrue(Arrays.equals(captor.getValue(), arr));
	}

}
