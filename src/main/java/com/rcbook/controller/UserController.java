package com.rcbook.controller;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.rcbook.configuration.PasswordService;
import com.rcbook.configuration.TokenHandler;
import com.rcbook.domain.User;
import com.rcbook.domain.UserCreateForm;
import com.rcbook.service.currentuser.CurrentUserDetailsService;
import com.rcbook.service.user.CarService;
import com.rcbook.service.user.RaceService;
import com.rcbook.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
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
    private CurrentUserDetailsService currentUserDetailsService;

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
    public Test payment() throws Exception {
        Map<String, String> sdkConfig = new HashMap<String, String>();
        sdkConfig.put("mode", "sandbox");
        String accessToken = new OAuthTokenCredential("AQkquBDf1zctJOWGKWUEtKXm6qVhueUEMvXO_-MCI4DQQ4-LWvkDLIN2fGsd", "EL1tVxAjhT7cJimnz5-Nsx9k2reTKSVfErNQF-CmrwJgxRtylkGTKlU4RvrX", sdkConfig).getAccessToken();

        APIContext apiContext = new APIContext(accessToken);
        apiContext.setConfigurationMap(sdkConfig);

        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal("12");

        Transaction transaction = new Transaction();
        transaction.setDescription("creating a payment");
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("https://devtools-paypal.com/guide/pay_paypal?cancel=true");
        redirectUrls.setReturnUrl("http://192.168.56.101:8080/demo-0.0.1-SNAPSHOT/payment2");
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
    public RedirectView executePayment(@RequestParam("paymentId") String id) throws Exception {
        Map<String, String> sdkConfig = new HashMap<String, String>();
        sdkConfig.put("mode", "sandbox");
        String accessToken = "Bearer A101.efzMtOG66PzLP0VOT8IsB5iWITK9JWx2qIqgHilLkPuT4-HoyzPcydQ9zsSrPMca.SSvvJdQKvMIu73a4DAsmNASu158";
        APIContext apiContext = new APIContext(accessToken);
        apiContext.setConfigurationMap(sdkConfig);

        Payment payment = new Payment();
        payment.setId(id);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId("Z7B2YXBPZ3HPS");
        Payment payment2 = payment.execute(apiContext, paymentExecute);
        payment2.getState();
        return new RedirectView("http://192.168.56.101:8080/demo-0.0.1-SNAPSHOT/");
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
