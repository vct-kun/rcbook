package com.rcbook.repository;

import com.rcbook.domain.Club;
import com.rcbook.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by vctran on 24/03/16.
 */
public interface ClubRepository extends JpaRepository<Club, Long> {

    Club findByOwner(User user);

    List<Club> findByUsers(User user);

}
