package com.rcbook.service.user;

import com.rcbook.domain.Driver;
import com.rcbook.domain.Race;
import com.rcbook.domain.User;

import java.util.List;

/**
 * Created by vctran on 24/03/16.
 */
public interface DriverService {

    Driver createDriver(Driver driver);

    List<Driver> findDriverByUser(User user);

    List<Driver> findDriverByRace(Race race);

    void deleteDriverById(Long driverId);
}
