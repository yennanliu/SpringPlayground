package lab.testability.staticblock.bad;

import java.util.Date;

import lab.testability.staticblock.StaticBlockDependency;

public class StaticBlockOwner {

	private static StaticBlockDependency dependency;
	static {
		dependency = new StaticBlockDependency();
		dependency.setLoadTime(new Date());
	}

	public boolean isLoadingTimeBefore(Date base) {
		return dependency.getLoadTime().before(base);
	}
}
