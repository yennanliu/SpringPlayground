package com.yen.ShoppingCart.service;

import static org.junit.jupiter.api.Assertions.*;

import com.yen.ShoppingCart.repository.TokenRepository;
import com.yen.ShoppingCart.repository.UserRepository;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Verifies that hashPassword still produces the correct MD5 hex output after
 * the javax.xml.bind.DatatypeConverter → java.util.HexFormat migration.
 *
 * The expected hashes are pre-computed MD5 values that the existing signIn
 * tests already rely on (e.g. "5F4DCC3B5AA765D61D8327DEB882CF99" for "password").
 */
class UserServicePasswordMigrationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, tokenRepository, authenticationService);
    }

    private String callHashPassword(String input) throws Exception {
        Method m = UserService.class.getDeclaredMethod("hashPassword", String.class);
        m.setAccessible(true);
        return (String) m.invoke(userService, input);
    }

    @Test
    void hashPassword_knownValue_returnsCorrectMD5() throws Exception {
        // "password" → MD5 → 5F4DCC3B5AA765D61D8327DEB882CF99
        // This exact value is used in UserServiceTest.testSignInNoToken / testSignInSuccess
        assertEquals("5F4DCC3B5AA765D61D8327DEB882CF99", callHashPassword("password"));
    }

    @Test
    void hashPassword_emptyString_returnsKnownMD5() throws Exception {
        // MD5("") = D41D8CD98F00B204E9800998ECF8427E
        assertEquals("D41D8CD98F00B204E9800998ECF8427E", callHashPassword(""));
    }

    @Test
    void hashPassword_isAlwaysUpperCase() throws Exception {
        String hash = callHashPassword("anyInput");
        assertEquals(hash, hash.toUpperCase(), "Hash must be uppercase (matching legacy DatatypeConverter output)");
    }

    @Test
    void hashPassword_lengthIs32Chars() throws Exception {
        // MD5 is always 128 bits = 32 hex chars
        assertEquals(32, callHashPassword("test").length());
        assertEquals(32, callHashPassword("").length());
        assertEquals(32, callHashPassword("a very long passphrase").length());
    }

    @Test
    void hashPassword_deterministicForSameInput() throws Exception {
        assertEquals(callHashPassword("hello"), callHashPassword("hello"));
    }

    @Test
    void hashPassword_differentInputsProduceDifferentHashes() throws Exception {
        assertNotEquals(callHashPassword("password1"), callHashPassword("password2"));
    }
}
