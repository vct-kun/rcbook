package com.rcbook.controller;

import com.rcbook.configuration.PasswordService;
import com.rcbook.configuration.TokenHandler;
import com.rcbook.domain.Club;
import com.rcbook.domain.User;
import com.rcbook.domain.UserCreateForm;
import com.rcbook.domain.VerificationToken;
import com.rcbook.service.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;

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
    private ClubService clubService;

    @Autowired
    private TokenHandler tokenHandler;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private TokenService tokenService;

    @Value("${application.domain.name}")
    private String domain;

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

    @RequestMapping(value = "/auth/signup", method = RequestMethod.POST)
    public ResponseEntity<Void> signup(@RequestBody UserCreateForm userCreateForm, HttpServletRequest request) {
        Optional<User> user = userService.getUserByEmail(userCreateForm.getEmail());
        if (!user.isPresent()) {
            User registred = userService.create(userCreateForm);
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registred, getBaseUrl(request).toString()));
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    public ResponseEntity<Token> login(@RequestBody UserCreateForm userCreateForm) {
        Optional<User> user = userService.getUserByEmail(userCreateForm.getEmail());
        if (user.isPresent() && PasswordService.checkPassword(userCreateForm.getPassword(), user.get().getPasswordHash())) {
            if (user.get().isEnabled()) {
                String token = tokenHandler.createTokenForUser(user.get());
                return ResponseEntity.ok(new Token(token));
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public @ResponseBody User getUserById(@PathVariable String id) {
        Optional<User> user = userService.getUserById(Long.valueOf(id));
        if (user.isPresent()) {
            List<Club> clubs = clubService.getListClubByUser(user.get());
            if (clubs!=null && !clubs.isEmpty()) {
                user.get().setUserHasClub(true);
            }
            user.get().setPremium(user.get().getAccount().equals("PREMIUM"));
            user.get().setOwner(user.get().getRole().name().equals("OWNER"));
            return user.get();
        }
        return null;
    }

    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public RedirectView confirmRegistration(@RequestParam("token") String token, HttpServletRequest request) {
        VerificationToken verificationToken = tokenService.getVerificationToken(token);
        if (verificationToken!=null) {
            User user = verificationToken.getUser();
            Calendar cal = Calendar.getInstance();
            if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
                //error
            }
            user.setEnabled(true);
            userService.update(user);

        }
        return new RedirectView(getBaseUrl(request).toString());
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

    private StringBuilder getBaseUrl(HttpServletRequest request) {
        StringBuilder baseUrl = new StringBuilder();
        baseUrl
                .append(request.getScheme())
                .append("://")
                .append(domain)
                .append(":")
                .append(request.getLocalPort())
                .append(request.getContextPath());
        return baseUrl;
    }

}
