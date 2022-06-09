package lab.testability.finals.good.klass;

public class UseFinalClassRefactored {
	private IFinalClass finalClass;

	public UseFinalClassRefactored(IFinalClass finalClass) {
		this.finalClass = finalClass;
	}

	public void doSomething() {
		finalClass.methodInFinalClass();
	}
}
