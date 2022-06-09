package lab.powermock.fFinal;

public class Installer {
	private final InstallVerifier verifier;
	public Installer(InstallVerifier verifier) {
		this.verifier = verifier;
	}

	public boolean install(String software) {
		if (verifier.isInstallable(software)) {
			// some install process
			return true;
		}
		return false;
	}
}
