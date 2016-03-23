package com.rcbook.service.user;

import com.rcbook.domain.Brand;
import com.rcbook.domain.Chassis;
import com.rcbook.repository.ChassisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by vctran on 23/03/16.
 */
@Service
public class ChassisServiceImpl implements ChassisService {

    @Autowired
    private ChassisRepository chassisRepository;


    @Override
    public List<Chassis> getChassisByBrand(Brand brand) {
        return chassisRepository.findByBrand(brand);
    }

    @Override
    public List<Chassis> getAllChassis() {
        return chassisRepository.findAll(new Sort("brand"));
    }
}
