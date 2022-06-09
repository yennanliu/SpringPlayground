package lab.testability.finals.bad.klass;

public class UseFinalClass {
	private FinalClassDemo finalClass;

	public UseFinalClass(FinalClassDemo finalClass) {
		this.finalClass = finalClass;
	}

	public void doSomething() {
		finalClass.methodInFinalClass();
	}
}
