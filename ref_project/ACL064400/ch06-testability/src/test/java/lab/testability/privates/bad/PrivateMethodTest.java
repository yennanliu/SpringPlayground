package lab.testability.privates.bad;

import org.junit.Before;
import org.junit.Test;

public class PrivateMethodTest {
	PrivateMethodDemo privateMethod;
	
	@Before
	public void setUp() {
		privateMethod = new PrivateMethodDemo();
	}
	
	@Test
	public void validate() throws Exception {
		privateMethod.validate(null);
	}
}
