package lab.testability.finals.good.method;

import static org.mockito.Mockito.doNothing;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UseFinalMethodRefactoredTest {

	@Mock
	NotFinalMethod notFinalMethod;
	
	FinalMethodDemoRefactored finalMethod;
	UseFinalMethodRefactored useFinalMethod;

	@Before
	public void setUp() {
		finalMethod = new FinalMethodDemoRefactored(notFinalMethod);
		useFinalMethod = new UseFinalMethodRefactored(finalMethod);
	}
	@Test
	public void show_final_method_test() throws Exception {
		doNothing().when(notFinalMethod).delegate();
		useFinalMethod.doSomething();
	}
}
