package com.rcbook.repository;

import com.rcbook.domain.User;
import com.rcbook.domain.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by vctran on 09/06/16.
 */
public interface TokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

}
