package com.rcbook.repository;

import com.rcbook.domain.Brand;
import com.rcbook.domain.Chassis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by vctran on 23/03/16.
 */
public interface ChassisRepository extends JpaRepository<Chassis, Long> {
    List<Chassis> findByBrand(Brand brand);
}
