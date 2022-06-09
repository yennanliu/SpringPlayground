package lab.testdoubles.spy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodAudit {

	// methodName & MethodInvocation
	private Map<String, List<MethodInvocation>> map = new HashMap<>();

	void registerCall(MethodInvocation invocation) {
		List<MethodInvocation> list = map.get(invocation.getMethod());
		if (list == null) {
			list = new ArrayList<>();
		}
		if (!list.contains(invocation)) {
			list.add(invocation);
		}
		map.put(invocation.getMethod(), list);
	}

	public int getInvocationQuantity(String methodName) {
		List<MethodInvocation> list = map.get(methodName);
		if (list == null) {
			return 0;
		}
		return list.size();
	}

	public MethodInvocation getInvocation(String methodName, int i) {
		List<MethodInvocation> list = map.get(methodName);
		if (list == null || (i > list.size())) {
			return null;
		}
		return list.get(i - 1);
	}
}
