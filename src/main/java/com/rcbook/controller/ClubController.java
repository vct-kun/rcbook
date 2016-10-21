package com.rcbook.controller;

import com.rcbook.domain.Club;
import com.rcbook.domain.Training;
import com.rcbook.domain.User;
import com.rcbook.service.user.ClubService;
import com.rcbook.service.user.UserService;
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
public class ClubController {

    @Autowired
    private ClubService clubService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/club", method = RequestMethod.POST)
    public @ResponseBody Club addClub(@RequestBody Club club) {
        Club createdClub = clubService.createClub(club);
        userService.updateRole(club.getOwner().getId());
        return createdClub;
    }

    @RequestMapping(value = "/club", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Club> getAllClubs() {
        return clubService.getAllClubs();
    }

    @RequestMapping(value = "/club/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Club getClub(@PathVariable String id) {
        Club club = clubService.getClubById(Long.valueOf(id));
        return club;
    }

    @RequestMapping(value = "/club/{id}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<Void> updateClub(@RequestBody Club clubUpdated) {
        clubService.createClub(clubUpdated);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/getOwnerClub", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Club getOwnerClub(@RequestParam String userId) {
        Optional<User> user = userService.getUserById(Long.valueOf(userId));
        return clubService.getClubByUser(user.get());
    }

    @RequestMapping(value = "/userHasClub", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody boolean userHasClub(@RequestParam String userId) {
        Optional<User> user = userService.getUserById(Long.valueOf(userId));
        List<Club> clubs = clubService.getListClubByUser(user.get());
        return clubs.size() > 0;
    }

    @RequestMapping(value = "/getClubsByUserId/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Club> getClubsByUserId(@PathVariable String id) {
        Optional<User> user = userService.getUserById(Long.valueOf(id));
        return clubService.getListClubByUser(user.get());
    }

    @RequestMapping(value = "/isUserInClub", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody boolean isUserInClub(@RequestParam("userId") String userId, @RequestParam("clubId") String clubId) {
        Optional<User> user = userService.getUserById(Long.valueOf(userId));
        List<Club> clubs = clubService.getListClubByUser(user.get());

        return clubs
                .stream()
                .anyMatch((club) -> club.getId() == Long.valueOf(clubId));
    }

    @RequestMapping(value = "/training", method = RequestMethod.POST)
    public @ResponseBody Training addTraining(@RequestBody Training training) {
        return clubService.addTraining(training);
    }

    @RequestMapping(value = "/getTrainingsByClub", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Training> getTrainingsByClub(@RequestParam String clubId) {
        Club club = clubService.getClubById(Long.valueOf(clubId));
        return clubService.getTrainingsByClub(club);
    }
}
