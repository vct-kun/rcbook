package com.rcbook.controller;

import com.rcbook.domain.*;
import com.rcbook.service.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Created by vctran on 25/03/16.
 */
@RestController
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private UserService userService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private ChassisService chassisService;

    @Autowired
    private MotorService motorService;

    @Autowired
    private EscService escService;

    @Autowired
    private SettingService settingService;

    @RequestMapping(value = "/getChassis", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Chassis> getChassisByBrand(@RequestParam String brandId) {
        Brand brand = brandService.getBrandById(Long.valueOf(brandId));
        return chassisService.getChassisByBrand(brand);
    }

    @RequestMapping(value = "/getBrands", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Brand> getBrands() {
        return brandService.getAllBrands();
    }

    @RequestMapping(value = "/car", method = RequestMethod.POST)
    public @ResponseBody Car addCar(@RequestBody Car car) {
        return carService.createCar(car);
    }

    @RequestMapping(value = "/getCarByUserId", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Car> getCar(@RequestParam String userId) {
        Optional<User> user = userService.getUserById(Long.valueOf(userId));
        return carService.getAllCarByUser(user.get());
    }

    @RequestMapping(value = "/car/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Car getCarDetails(@PathVariable String id) {
        return carService.getCarById(Long.valueOf(id));
    }

    @RequestMapping(value = "/getMotors", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Motor> getMotors() {
        return motorService.getMotors();
    }

    @RequestMapping(value = "/getEsc", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Esc> getEsc() {
        return escService.getEsc();
    }

    @RequestMapping(value = "/setting", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody Setting addSetting(@RequestBody Setting setting) {
        return settingService.createSetting(setting);
    }

    @RequestMapping(value = "/getSettingsByCarId/{carId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Setting> getSettingsByCar(@PathVariable String carId) {
        Car car = new Car();
        car.setId(Long.valueOf(carId));
        return settingService.getSettingsByCar(car);
    }
}
