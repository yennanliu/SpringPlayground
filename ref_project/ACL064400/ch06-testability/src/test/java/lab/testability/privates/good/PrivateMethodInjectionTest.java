package lab.testability.privates.good;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PrivateMethodInjectionTest {
	@Mock
	GraphicalInterface graphicalInterface;
	@InjectMocks
	PrivateMethodInjection privateMethod;
	@Test
	public void validate() throws Exception {
		privateMethod.validate(null);
	}
}
