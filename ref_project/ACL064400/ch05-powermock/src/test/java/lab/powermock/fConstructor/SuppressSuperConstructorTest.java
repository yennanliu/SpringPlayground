package lab.powermock.fConstructor;

import static org.powermock.api.support.membermodification.MemberMatcher.constructor;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SuppressSuperConstructor.class)
public class SuppressSuperConstructorTest {

	@Test
	public void show_supresses_super_class_constructor() {
		suppress(constructor(DoNotExtendMe.class));
		new SuppressSuperConstructor();
	}
}
