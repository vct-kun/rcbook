package com.rcbook.repository;

import com.rcbook.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by VanCharles on 2016/03/23.
 */
public interface CarRepository extends JpaRepository<Car, Long> {
}
