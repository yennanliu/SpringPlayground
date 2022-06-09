package lab.testability.finals.bad.klass;

import static org.mockito.Mockito.doNothing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UseFinalClassTest {
	@Mock
	FinalClassDemo finalClass;
	@InjectMocks
	UseFinalClass useFinalClass;
	@Test
	public void show_final_class_test() throws Exception {
		doNothing().when(finalClass).methodInFinalClass();
		useFinalClass.doSomething();
	}
}
