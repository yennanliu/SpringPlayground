package lab.powermock.fPrivate;

public class PrivateMethod {

	private String secretValue() {
		return "!@#$%^&";
	}

	public String exposeSecretValue() {
		return secretValue();
	}
}
