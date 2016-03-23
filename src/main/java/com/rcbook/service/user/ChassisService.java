package com.rcbook.service.user;

import com.rcbook.domain.Brand;
import com.rcbook.domain.Chassis;

import java.util.List;

/**
 * Created by vctran on 23/03/16.
 */
public interface ChassisService {
    List<Chassis> getChassisByBrand(Brand brand);

    List<Chassis> getAllChassis();
}
