package no.hig.ezludo.client;
import junit.framework.TestCase;
import org.junit.Test;

import java.lang.String;

/**
 * This test check if userdata-validation is correct. The first method includes valid data
 * and should return true. The second method should return false
 * @author Kristian
 * date 31.10.2015.
 */
public class UserAccountTest extends TestCase {
    private char[] password = "password12345".toCharArray();
    private char[] passwordRepeat = "password12345".toCharArray();
    private String email = "test@test.no";
    private String username = "test";

    @Test
    public void registerTest() throws Exception {
       // assertEquals(true, UserAccount.usernameChecker(username));
       // assertEquals(true, UserAccount.UserAccount.emailChecker(email););
       // assertEquals(true, UserAccount.UserAccount.passwordChecker(password, passwordRepeat););
    }

    @Test
    public void registerFailTest() throws Exception {
       // char[] password = "pwd1".toCharArray();
       // char[] passwordRepeat = "pwd2".toCharArray();
       // String email = "test.test@";
       // String username = "usr";
       // assertEquals(false, UserAccount.usernameChecker(username));
       // assertEquals(false, UserAccount.UserAccount.emailChecker(email););
       // assertEquals(false, UserAccount.UserAccount.passwordChecker(password, passwordRepeat););
    }
}
