package lab.powermock.fFinal;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FinalClassTestByMockito {
	@Test
	public void show_mocks_final_classes() throws Exception {
		// Given
		InstallVerifier systemVerifier = mock(InstallVerifier.class);
		when(systemVerifier.isInstallable("JDK")).thenReturn(true);
		// When
		Installer installer = new Installer(systemVerifier);
		// Then
		assertTrue(installer.install("JDK"));
	}
}
