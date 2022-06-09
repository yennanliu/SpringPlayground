package lab.mockito.more;

public class LDAPManagerImpl implements LDAPManager {

	@Override
	public boolean isValidUser(String userName, String encrypterPassword) {
		return false;
	}

}
