package com.rcbook.controller;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @RequestMapping(value = "/payment", method = RequestMethod.GET)
    public Test payment(HttpServletRequest request) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        StringBuilder baseUrl = new StringBuilder();
        baseUrl
                .append(request.getScheme())
                .append("://")
                .append(request.getLocalName())
                .append(":")
                .append(request.getLocalPort())
                .append(request.getContextPath());

        Map<String, String> sdkConfig = new HashMap<>();
        sdkConfig.put("mode", "sandbox");
        String accessToken = new OAuthTokenCredential("AQkquBDf1zctJOWGKWUEtKXm6qVhueUEMvXO_-MCI4DQQ4-LWvkDLIN2fGsd", "EL1tVxAjhT7cJimnz5-Nsx9k2reTKSVfErNQF-CmrwJgxRtylkGTKlU4RvrX", sdkConfig).getAccessToken();

        APIContext apiContext = new APIContext(accessToken);
        apiContext.setConfigurationMap(sdkConfig);

        Amount amount = new Amount();
        amount.setCurrency("EUR");
        amount.setTotal("10");

        Transaction transaction = new Transaction();
        transaction.setDescription("creating a payment");
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(baseUrl.toString()+"/#/home");
        User user = (User) authentication.getDetails();
        redirectUrls.setReturnUrl(baseUrl.toString()+"/payment2?userId="+user.getId());
        payment.setRedirectUrls(redirectUrls);

        Payment createdPayment = payment.create(apiContext);
        for (Links links : createdPayment.getLinks()) {
            if (links.getMethod().equals("REDIRECT")) {
                Test test = new Test();
                test.setUrl(links.getHref());
                return test;
            }
        }
        return null;
    }

    @RequestMapping(value = "/payment2", method = RequestMethod.GET)
    public RedirectView executePayment(@RequestParam("paymentId") String id, @RequestParam("PayerID") String payerId, @RequestParam("userId") String userId) throws Exception {
        System.out.println("UserId:"+userId);
        Map<String, String> sdkConfig = new HashMap<>();
        sdkConfig.put("mode", "sandbox");
        String accessToken = new OAuthTokenCredential("AQkquBDf1zctJOWGKWUEtKXm6qVhueUEMvXO_-MCI4DQQ4-LWvkDLIN2fGsd", "EL1tVxAjhT7cJimnz5-Nsx9k2reTKSVfErNQF-CmrwJgxRtylkGTKlU4RvrX", sdkConfig).getAccessToken();
        APIContext apiContext = new APIContext(accessToken);
        apiContext.setConfigurationMap(sdkConfig);

        Payment payment = new Payment();
        payment.setId(id);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        Payment payment2 = payment.execute(apiContext, paymentExecute);
        if (payment2!=null) {
            if ("approved".equals(payment2.getState())) {
                Optional<User> user = userService.getUserById(Long.valueOf(userId));
                if (user.isPresent()) {
                    user.get().setAccount("PREMIUM");
                    userService.update(user.get());
                }
            }
        }
        return new RedirectView("#/home");
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

    class Test {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
