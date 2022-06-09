package lab.testability.staticblock.bad;

import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import lab.testability.staticblock.StaticBlockDependency;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("lab.testability.staticblock.bad.StaticBlockOwner")
public class StaticBlockOwnerTestByPowerMock {
	@Test
	public void Should_return_true_When_given_earlier_than_loading_time() throws ParseException {
		// Given
		Date loadTime = new SimpleDateFormat("yyyy-MM-dd").parse("2019-05-29");
		StaticBlockDependency dependency = mock(StaticBlockDependency.class);
		when(dependency.getLoadTime()).thenReturn(loadTime);

		Whitebox.setInternalState(StaticBlockOwner.class, dependency);

		// When
		StaticBlockOwner instance = new StaticBlockOwner();
		// Then
		Date base = new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-29");
		assertTrue(instance.isLoadingTimeBefore(base));
	}
}
