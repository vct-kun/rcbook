package com.rcbook.controller;

import com.rcbook.domain.Brand;
import com.rcbook.domain.Car;
import com.rcbook.domain.Chassis;
import com.rcbook.domain.User;
import com.rcbook.service.user.BrandService;
import com.rcbook.service.user.CarService;
import com.rcbook.service.user.ChassisService;
import com.rcbook.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
