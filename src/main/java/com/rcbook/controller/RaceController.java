package com.rcbook.controller;

import com.rcbook.domain.Club;
import com.rcbook.domain.Driver;
import com.rcbook.domain.Race;
import com.rcbook.service.user.ClubService;
import com.rcbook.service.user.DriverService;
import com.rcbook.service.user.RaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
