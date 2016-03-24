package com.rcbook.service.user;

import com.rcbook.domain.Car;
import com.rcbook.domain.User;
import com.rcbook.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by VanCharles on 2016/03/23.
 */
@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private CarRepository carRepository;

    @Override
    public Car createCar(Car car) {
        return carRepository.save(car);
    }

    @Override
    public List<Car> getAllCarByUser(User user) {
        return carRepository.findByUser(user);
    }

    @Override
    public Long countCarByUser(User user) {
        List<Car> cars = carRepository.findByUser(user);
        return Long.valueOf(cars.size());
    }
}
