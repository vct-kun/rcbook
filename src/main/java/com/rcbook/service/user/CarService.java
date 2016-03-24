package com.rcbook.service.user;

import com.rcbook.domain.Car;
import com.rcbook.domain.User;

import java.util.List;

/**
 * Created by VanCharles on 2016/03/23.
 */
public interface CarService {

    Car createCar(Car car);

    List<Car> getAllCarByUser(User user);

    Long countCarByUser(User user);

}
