package lab;

public class Calculator4Assumption {

	public static int add(int a, int b) {
		return a + b;
		// return a + b - 1;
	}

	public static int multiply(int a, int b) {
		int result = 0;
		for (int i = 0; i < b; i++) {
			result = add(result, a);
		}
		return result;
		// return result - 1;
	}

}
