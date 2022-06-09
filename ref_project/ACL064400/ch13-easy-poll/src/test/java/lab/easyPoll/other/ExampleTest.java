package lab.easyPoll.other;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest
@WebAppConfiguration
public class ExampleTest {
	@BeforeAll
	public static void setup() {
	}

	@Test
	public void testSomeThing() {
		assertTrue(true);
	}

	@AfterAll
	public static void teardown() {
	}
}
