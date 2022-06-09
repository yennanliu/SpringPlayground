package lab.testability.staticblock.bad;

import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class StaticBlockOwnerTest {

	StaticBlockOwner staticBlockDemo;

	@Before
	public void setUp() {
		staticBlockDemo = new StaticBlockOwner();
	}

	@Test
	public void testLoadingTime() throws ParseException {
		Date base = new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-29");
		assertTrue(staticBlockDemo.isLoadingTimeBefore(base));
	}
}
