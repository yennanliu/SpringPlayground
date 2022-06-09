package lab.testability.instantiate.bad;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.powermock.api.support.membermodification.MemberMatcher.field;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

import java.lang.reflect.Field;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import lab.testability.DatabaseDependency;
import lab.testability.TestingImpedimentException;

@RunWith(PowerMockRunner.class)
@PrepareForTest(BadVariableInitialization.class)
public class BadVariableInitializationTestPowerMock {

	@Mock
	DatabaseDependency dependency;

	@Test
	public void show_object_is_not_null() throws Exception {
		PowerMockito
			.whenNew(DatabaseDependency.class)
			.withNoArguments()
			.thenReturn(dependency);

		BadVariableInitialization instance = new BadVariableInitialization();
		assertNotNull(instance);
		assertEquals("Jim", instance.someMethod("Jim"));
	}


	@Test(expected = TestingImpedimentException.class)
	public void show_suppress_field_is_not_working() throws Exception {
		suppress(field(BadVariableInitialization.class, "dependency1"));
		new BadVariableInitialization();
	}

	@Test(expected = TestingImpedimentException.class)
	public void show_reset_field_is_not_working() throws Exception {
		BadVariableInitialization instance = new BadVariableInitialization();
		Field field = PowerMockito.field(BadVariableInitialization.class, "dependency1");
		DatabaseDependency mock = PowerMockito.mock(DatabaseDependency.class);
		field.set(instance, mock);
	}

	@Test(expected = TestingImpedimentException.class)
	public void show_setInternalState_is_not_working() throws Exception {
		BadVariableInitialization instance = new BadVariableInitialization();
		DatabaseDependency mock = PowerMockito.mock(DatabaseDependency.class);
		Whitebox.setInternalState(instance, "dependency1", mock);
	}

}
