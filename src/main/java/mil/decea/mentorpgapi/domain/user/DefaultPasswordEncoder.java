package mil.decea.mentorpgapi.domain.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DefaultPasswordEncoder {

    private static final PasswordEncoder defaultPasswordEncoder = new BCryptPasswordEncoder();
    public static String encode(String notEncodedPassword){

        return  getDefaultPasswordEncoder().encode(notEncodedPassword);
    }

    public static PasswordEncoder getDefaultPasswordEncoder() {
        return defaultPasswordEncoder;
    }
    public static boolean matchesPasswords(String notEncodedPassword, String encodedPassword){
        return defaultPasswordEncoder.matches(notEncodedPassword, encodedPassword);
    }
}
