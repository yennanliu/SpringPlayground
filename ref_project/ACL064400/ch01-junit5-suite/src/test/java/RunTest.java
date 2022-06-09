
import static org.junit.platform.engine.discovery.ClassNameFilter.includeClassNamePatterns;
//import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
//import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

import java.io.PrintWriter;

import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import lab.CalculatorAddTest;

public class RunTest {

	SummaryGeneratingListener listener = new SummaryGeneratingListener();

	private void testSelectClass() {
		LauncherDiscoveryRequest request = 
				LauncherDiscoveryRequestBuilder
				.request()
				.selectors(DiscoverySelectors.selectClass(CalculatorAddTest.class))
				.build();
		Launcher launcher = LauncherFactory.create();
		launcher.registerTestExecutionListeners(listener);
		launcher.execute(request);
	}

	private void testSelectPackage() {
		LauncherDiscoveryRequest request = 
				LauncherDiscoveryRequestBuilder
				.request()
				.selectors(DiscoverySelectors.selectPackage("lab"))
				.filters(includeClassNamePatterns(".*AddTest"))
				.build();
		Launcher launcher = LauncherFactory.create();
		launcher.registerTestExecutionListeners(listener);
		launcher.execute(request);
	}

	public static void main(String[] args) {
		RunTest runner = new RunTest();
		TestExecutionSummary summary = null;

		System.out.println("testSelectClass(): ");
		runner.testSelectClass();
		summary = runner.listener.getSummary();
		summary.printTo(new PrintWriter(System.out));

		System.out.println("testSelectPackage(): ");
		runner.testSelectPackage();
		summary = runner.listener.getSummary();
		summary.printTo(new PrintWriter(System.out));
	}
}
