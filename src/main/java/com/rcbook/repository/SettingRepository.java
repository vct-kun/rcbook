package com.rcbook.repository;

import com.rcbook.domain.Car;
import com.rcbook.domain.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by vctran on 23/05/16.
 */
public interface SettingRepository extends JpaRepository<Setting, Long> {

    List<Setting> findByCar(Car car);

}
