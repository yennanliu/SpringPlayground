package lab.testability.staticmethods;

public class StaticMethodDemo2 {

	public static void aVoid() {
		throw new RuntimeException();
	}

	public static String aReturn() {
		return "Hello";
	}

	public static String aParamReturn(String s) {
		return "Hello " + s;
	}

}
