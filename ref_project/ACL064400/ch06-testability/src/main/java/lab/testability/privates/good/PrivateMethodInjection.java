package lab.testability.privates.good;

public class PrivateMethodInjection {
	
	private GraphicalInterface graphicalInterface;
	public PrivateMethodInjection(GraphicalInterface ui) {
		this.graphicalInterface = ui;
	}
	
	public Object validate(Object arg) {
		if (arg == null) {
			showError("Null input");
		}
		return arg;
	}

	private void showError(String msg) {
		graphicalInterface.showMessage(msg);
	}
}
