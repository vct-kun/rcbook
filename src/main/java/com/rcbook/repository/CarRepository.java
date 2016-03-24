package com.rcbook.repository;

import com.rcbook.domain.Car;
import com.rcbook.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by VanCharles on 2016/03/23.
 */
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByUser(User user);
}
