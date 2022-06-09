package lab.powermock.fStatic;

import java.util.HashMap;
import java.util.Map;

public class StaticInitDemo {
	
	static int value = 10;
	
	static final Map<String, String> MAP = new HashMap<>();
	static {		
		MAP.put("a", "honey");
		MAP.put("b", "jelly");
		MAP.put("c", "beans");
	}
}
