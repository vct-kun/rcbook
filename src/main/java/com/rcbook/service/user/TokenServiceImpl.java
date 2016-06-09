package com.rcbook.service.user;

import com.rcbook.domain.User;
import com.rcbook.domain.VerificationToken;
import com.rcbook.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by vctran on 09/06/16.
 */
@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public void save(VerificationToken verificationToken) {
        tokenRepository.save(verificationToken);
    }

    @Override
    public VerificationToken getVerificationToken(String token) {
        return tokenRepository.findByToken(token);
    }

}
