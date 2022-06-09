package lab.powermock;

public class SuppressMethod {

	public String format(String str) {
		return str + getCurrency();
	}
	private String getCurrency() {
		return "$";
	}

	public int plusNumber(int i) {
		return i + getNumber();
	}
	private int getNumber() {
		return 10;
	}
}
