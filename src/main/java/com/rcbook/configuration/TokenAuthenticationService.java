package com.rcbook.configuration;

import com.rcbook.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by vctran on 05/04/16.
 */
@Service
public class TokenAuthenticationService {

    @Autowired
    private TokenHandler tokenHandler;

    public Authentication getAuthentication(HttpServletRequest request) {
        final String token = request.getHeader("Authorization");
        if (token != null) {
            final User user = tokenHandler.parseUserFromToken(token.split(" ")[1]);
            if (user != null) {
                return new UserAuthentication(user);
            }
        }
        return null;
    }

}
