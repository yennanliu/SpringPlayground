package lab.powermock.fConstructor;

public class SuppressConstructor {

	private int someInt = 100;
	private boolean someBoolean;

	public SuppressConstructor(int val) {
		someBoolean = true;
		someInt = someInt / val;
	}
	public SuppressConstructor() {
		someBoolean = true;
		someInt = someInt / 0;
	}

	public int getSomeInt() {
		return someInt;
	}
	public boolean isSomeBoolean() {
		return someBoolean;
	}
}
