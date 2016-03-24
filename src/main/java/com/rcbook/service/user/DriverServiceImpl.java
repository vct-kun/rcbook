package com.rcbook.service.user;

import com.rcbook.domain.Driver;
import com.rcbook.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
