package com.rcbook.configuration;

import com.rcbook.domain.User;
import com.rcbook.service.user.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by vctran on 05/04/16.
 */
@Component
public class TokenHandler {
    @Value("mySecret")
    private String secret;

    @Autowired
    private UserService userService;

    public User parseUserFromToken(String token) {
        String id = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        Optional<User> user = userService.getUserById(Long.valueOf(id));
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    public String createTokenForUser(User user) {
        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
