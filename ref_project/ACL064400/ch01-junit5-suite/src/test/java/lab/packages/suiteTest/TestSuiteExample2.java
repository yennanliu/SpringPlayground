package lab.packages.suiteTest;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

import lab.packages.packageA.ClassATest;
import lab.packages.packageB.ClassBTest;

@RunWith(JUnitPlatform.class)
@SelectClasses({ ClassATest.class, ClassBTest.class })
public class TestSuiteExample2 {

}
