package lab.powermock.fFinal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FinalMethodTestByMockito {
	@Test
	public void stubs_final_methods() throws Exception {
		// Given
		FinalMethod finalMethod = mock(FinalMethod.class);
		when(finalMethod.getValue()).thenReturn("A stubbed value");
		// When & Then
		assertEquals("A stubbed value", finalMethod.getValue());
	}
}
