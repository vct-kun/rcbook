package com.rcbook.repository;

import com.rcbook.domain.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by vctran on 24/03/16.
 */
public interface DriverRepository extends JpaRepository<Driver, Long> {
}
