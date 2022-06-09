package lab.powermock.fConstructor;

public class SuppressSuperConstructor extends DoNotExtendMe {
	public SuppressSuperConstructor() {
		super();
	}
}

class DoNotExtendMe {
	DoNotExtendMe() {
		System.out.println(1 / 0);
	}
}
