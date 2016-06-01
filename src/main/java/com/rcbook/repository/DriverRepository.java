package com.rcbook.repository;

import com.rcbook.domain.Driver;
import com.rcbook.domain.Race;
import com.rcbook.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by vctran on 24/03/16.
 */
public interface DriverRepository extends JpaRepository<Driver, Long> {

    List<Driver> findByUser(User user);

    List<Driver> findByRace(Race race);

}
