package lab.testability.finals.good.method;

public class FinalMethodDemoRefactored {
	
	private NotFinalMethod notFinalMethod;
	
	public FinalMethodDemoRefactored (NotFinalMethod notFinalMethod) {
		this.notFinalMethod = notFinalMethod;
	}

	public final void aFinalMethod() {
		notFinalMethod.delegate();
	}
}
