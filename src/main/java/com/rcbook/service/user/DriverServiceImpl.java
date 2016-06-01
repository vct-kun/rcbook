package com.rcbook.service.user;

import com.rcbook.domain.Driver;
import com.rcbook.domain.Race;
import com.rcbook.domain.User;
import com.rcbook.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by vctran on 24/03/16.
 */
@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Override
    public Driver createDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    @Override
    public List<Driver> findDriverByUser(User user) {
        return driverRepository.findByUser(user);
    }

    @Override
    public List<Driver> findDriverByRace(Race race) {
        return driverRepository.findByRace(race);
    }

    @Override
    public void deleteDriverById(Long driverId) {
        driverRepository.delete(driverId);
    }
}
