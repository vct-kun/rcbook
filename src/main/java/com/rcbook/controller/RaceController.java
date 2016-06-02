package com.rcbook.controller;

import com.rcbook.domain.Club;
import com.rcbook.domain.Driver;
import com.rcbook.domain.Race;
import com.rcbook.domain.User;
import com.rcbook.service.user.ClubService;
import com.rcbook.service.user.DriverService;
import com.rcbook.service.user.RaceService;
import com.rcbook.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by vctran on 25/03/16.
 */
@RestController
public class RaceController {

    @Autowired
    private RaceService raceService;

    @Autowired
    private ClubService clubService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/race", method = RequestMethod.POST)
    public @ResponseBody Race addRace(@RequestBody Race race) {
        return raceService.createRace(race);
    }

    @RequestMapping(value = "/getRacesByClub", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Race> getRacesbyClub(@RequestParam String clubId) {
        Club club = clubService.getClubById(Long.valueOf(clubId));
        return raceService.getRacesByClub(club);
    }

    @RequestMapping(value = "/race/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Race getRace(@PathVariable String id) {
        return raceService.getRaceById(Long.valueOf(id));
    }

    @RequestMapping(value = "/race/{id}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<Void> updateRace(@RequestBody Race raceUpdated) {
        raceService.createRace(raceUpdated);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/race", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Race> getAllRaces() {
        return raceService.getAllRaces();
    }

    @RequestMapping(value = "/driver", method = RequestMethod.POST)
    public @ResponseBody Driver addDriver(@RequestBody Driver driver) {
        return driverService.createDriver(driver);
    }

    @RequestMapping(value = "/isUserInRace/{raceId}/{userId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Driver isUserInRace(@PathVariable String raceId, @PathVariable String userId) {
        Optional<User> user = userService.getUserById(Long.valueOf(userId));
        if (user.isPresent()) {
            List<Driver> drivers = driverService.findDriverByUser(user.get());
            for (Driver driver : drivers) {
                if (driver.getRace().getId() == Long.valueOf(raceId)) {
                    return driver;
                }
            }
        }
        return null;
    }

    @RequestMapping(value = "/getDriversByRace", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Driver> getDriversByRace(@RequestParam String raceId) {
        Race race = raceService.getRaceById(Long.valueOf(raceId));
        if (race!=null) {
            return driverService.findDriverByRace(race);
        }
        return null;
    }

    @RequestMapping(value = "/driver/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteDriver(@PathVariable String id) {
        driverService.deleteDriverById(Long.valueOf(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/getRacesByUserId", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Driver> getRacesByUserId(@RequestParam String userId) {
        Optional<User> user = userService.getUserById(Long.valueOf(userId));
        if (user.isPresent()) {
            return driverService.findDriverByUser(user.get());
        }
        return new ArrayList<>();
    }
}
