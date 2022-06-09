package lab.mockito.more;

public class Calculator {

	public int multiply(int a, int b) {
		int result = 0;
		for (int i = 0; i < b; i++) {
			result = add(a, result);
		}
		return result;
	}

	public int add(int a, int b) {
		return a + b;
	}
	
}