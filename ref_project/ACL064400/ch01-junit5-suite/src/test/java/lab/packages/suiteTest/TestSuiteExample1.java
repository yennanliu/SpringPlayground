package lab.packages.suiteTest;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectPackages({ "lab.packages.packageA", "lab.packages.packageB" })
public class TestSuiteExample1 {
}
