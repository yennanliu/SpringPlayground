package lab.testability.finals.good.method;

public class UseFinalMethodRefactored {

	private FinalMethodDemoRefactored finalMethod;

	public UseFinalMethodRefactored(FinalMethodDemoRefactored finalMethod) {
		this.finalMethod = finalMethod;
	}
	
	public void doSomething() {
		finalMethod.aFinalMethod();
	}
}
