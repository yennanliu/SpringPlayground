package lab.testability.finals.bad.method;

public class UseFinalMethod {

	private FinalMethodDemo finalMethod;

	public UseFinalMethod(FinalMethodDemo finalMethod) {
		this.finalMethod = finalMethod;
	}
	
	public void doSomething() {
		finalMethod.aFinalMethod();
	}
}
