package com.rcbook.repository;

import com.rcbook.domain.Motor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by vctran on 23/05/16.
 */
public interface MotorRepository extends JpaRepository<Motor, Long> {
}
