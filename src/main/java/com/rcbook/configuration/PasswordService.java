package com.rcbook.configuration;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * Created by vctran on 05/04/16.
 */
public class PasswordService {

    public static String hashPassword(String plainText) {
        return BCrypt.hashpw(plainText, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainText, String hashed) {
        return BCrypt.checkpw(plainText, hashed);
    }

}
