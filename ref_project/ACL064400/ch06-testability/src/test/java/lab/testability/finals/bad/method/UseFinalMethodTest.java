package lab.testability.finals.bad.method;

import static org.mockito.Mockito.doNothing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UseFinalMethodTest {
	@Mock
	FinalMethodDemo finalMethod;
	@InjectMocks
	UseFinalMethod useFinalMethod;
	@Test
	public void show_final_method_test() throws Exception {
		doNothing().when(finalMethod).aFinalMethod();
		useFinalMethod.doSomething();
	}
}
