package lab.testability.newexpression.bad;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UseNewExpressionTest {

	UseNewExpression useNewExpression;

	@Before
	public void setUp() {
		useNewExpression = new UseNewExpression();
	}

	@Test
	public void show_new_expression_test() throws Exception {
		useNewExpression.doSomething();
	}
}
