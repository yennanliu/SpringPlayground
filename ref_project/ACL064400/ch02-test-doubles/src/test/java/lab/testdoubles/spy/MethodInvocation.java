package lab.testdoubles.spy;

import java.util.ArrayList;
import java.util.List;

public class MethodInvocation {

	private List<Object> params = new ArrayList<>();
	private Object returnedValue;
	private String method;

	public List<Object> getParams() {
		return params;
	}

	public Object getReturnedValue() {
		return returnedValue;
	}

	public String getMethod() {
		return method;
	}

	public MethodInvocation addParam(Object parm) {
		getParams().add(parm);
		return this;
	}

	public MethodInvocation setReturnedValue(Object val) {
		this.returnedValue = val;
		return this;
	}

	public MethodInvocation setMethod(String method) {
		this.method = method;
		return this;
	}
}
