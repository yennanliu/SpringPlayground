package lab.testability.newexpression.good;

import lab.testability.newexpression.NewExpression;

public class UseNewExpressionRefactored {

	private NewExpression stuff;

	public UseNewExpressionRefactored(NewExpression stuff) {
		this.stuff = stuff;
	}

	public void doSomething() {
		stuff.myMethod();
	}
}
