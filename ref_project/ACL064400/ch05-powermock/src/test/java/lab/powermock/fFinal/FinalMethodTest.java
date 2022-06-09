package lab.powermock.fFinal;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FinalMethod.class)
public class FinalMethodTest {

	@Test
	public void show_stub_final_methods() throws Exception {
		// Given
		FinalMethod finalMethod = mock(FinalMethod.class);
		when(finalMethod.getValue()).thenReturn("A stubbed value");
		// When & Then
		assertEquals("A stubbed value", finalMethod.getValue());
	}
}
