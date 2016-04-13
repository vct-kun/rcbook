package com.rcbook.controller;

import com.rcbook.configuration.PasswordService;
import com.rcbook.configuration.TokenHandler;
import com.rcbook.domain.User;
import com.rcbook.domain.UserCreateForm;
import com.rcbook.service.user.CarService;
import com.rcbook.service.user.RaceService;
import com.rcbook.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by vctran on 25/03/16.
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @Autowired
    private RaceService raceService;

    @Autowired
    private TokenHandler tokenHandler;

    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }

    @RequestMapping("/dashboard")
    public Map<String, Object> home(@RequestParam String userId) {
        Optional<User> user = userService.getUserById(Long.valueOf(userId));
        Map<String, Object> model = new HashMap<>();
        model.put("nbRace", raceService.countRaces());
        model.put("nbCar", carService.countCarByUser(user.get()));
        return model;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody UserCreateForm userCreateForm, HttpServletRequest request) {
        Optional<User> user = userService.getUserByEmail(userCreateForm.getEmail());
        if (!user.isPresent()) {
            userService.create(userCreateForm);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    public ResponseEntity<Token> login(@RequestBody UserCreateForm userCreateForm) {
        Optional<User> user = userService.getUserByEmail(userCreateForm.getEmail());
        if (user.isPresent() && PasswordService.checkPassword(userCreateForm.getPassword(), user.get().getPasswordHash())) {
            String token = tokenHandler.createTokenForUser(user.get());
            return ResponseEntity.ok(new Token(token));
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public @ResponseBody User getUserById(@PathVariable String id) {
        Optional<User> user = userService.getUserById(Long.valueOf(id));
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    class Token {
        private String token;

        public Token(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

}
