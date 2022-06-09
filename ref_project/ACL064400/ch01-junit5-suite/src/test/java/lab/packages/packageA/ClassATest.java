package lab.packages.packageA;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class ClassATest {
	@Tag("production")
	@Test
	@DisplayName("packageA > ClassATest")
	public void testCaseA() {
	}
}
