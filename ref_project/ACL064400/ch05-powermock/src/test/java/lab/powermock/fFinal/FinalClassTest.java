package lab.powermock.fFinal;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(InstallVerifier.class)
public class FinalClassTest {

	@Test
	public void show_mocks_final_classes() throws Exception {
		// Given
		InstallVerifier verifier = mock(InstallVerifier.class);
		when(verifier.isInstallable("JDK")).thenReturn(true);
		// When
		Installer installer = new Installer(verifier);
		// Then
		assertTrue(installer.install("JDK"));
	}
}
