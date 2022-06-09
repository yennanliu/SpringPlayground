package lab.testability.newexpression.good;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import lab.testability.newexpression.NewExpression;

@RunWith(MockitoJUnitRunner.class)
public class UseNewExpressionRefactoredTest {

	@Mock
	NewExpression newExpression;

	@InjectMocks
	UseNewExpressionRefactored useNewExpression;

	@Test
	public void show_new_expression_test() throws Exception {
		useNewExpression.doSomething();
	}
}
