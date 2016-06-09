package com.rcbook.service.user;

import com.rcbook.domain.VerificationToken;

/**
 * Created by vctran on 09/06/16.
 */
public interface TokenService {

    void save(VerificationToken verificationToken);

    VerificationToken getVerificationToken(String token);

}
