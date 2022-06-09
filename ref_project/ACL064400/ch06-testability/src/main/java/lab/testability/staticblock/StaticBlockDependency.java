package lab.testability.staticblock;

import java.util.Date;

import lab.testability.TestingImpedimentException;

public class StaticBlockDependency {
	private Date loadTime;

	public StaticBlockDependency() {
		throw new TestingImpedimentException("Can't be loaded!!");
	}

	public Date getLoadTime() {
		return loadTime;
	}

	public void setLoadTime(Date loadTime) {
		this.loadTime = loadTime;
	}

}
