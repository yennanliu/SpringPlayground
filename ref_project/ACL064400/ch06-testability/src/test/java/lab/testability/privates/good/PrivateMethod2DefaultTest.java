package lab.testability.privates.good;

import org.junit.Before;
import org.junit.Test;

public class PrivateMethod2DefaultTest {

	PrivateMethod2Default privateMethod;

	@Before
	public void setUp() {
		privateMethod = new PrivateMethod2Default() {
			void showError(String msg) {
				// do nothing
			}
		};
	}

	@Test
	public void validate() throws Exception {
		privateMethod.validate(null);
	}
}
