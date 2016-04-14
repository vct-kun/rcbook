package com.rcbook.controller;

import com.paypal.api.payments.*;
import com.paypal.api.payments.Currency;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.rcbook.domain.User;
import com.rcbook.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by vctran on 13/04/16.
 */
@RestController
public class PaymentController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/createPlan", method = RequestMethod.GET)
    public Plan createPlan(HttpServletRequest request) throws Exception {
        StringBuilder baseUrl = new StringBuilder();
        baseUrl
                .append(request.getScheme())
                .append("://")
                .append(request.getLocalName())
                .append(":")
                .append(request.getLocalPort())
                .append(request.getContextPath());

        Plan plan = new Plan();
        plan.setName("Rcbook plan");
        plan.setDescription("Monthly plan for rcbook");
        plan.setType("fixed");

        List<PaymentDefinition> payments = new ArrayList<>();
        PaymentDefinition paymentDefinition = new PaymentDefinition();
        paymentDefinition.setName("Standard rcbook plan");
        paymentDefinition.setType("regular");
        paymentDefinition.setFrequencyInterval("1");
        paymentDefinition.setFrequency("MONTH");
        paymentDefinition.setCycles("11");
        Currency amount = new Currency();
        amount.setCurrency("EUR");
        amount.setValue("10");
        paymentDefinition.setAmount(amount);
        payments.add(paymentDefinition);

        plan.setPaymentDefinitions(payments);
        MerchantPreferences merchantPreferences = new MerchantPreferences();
        merchantPreferences.setCancelUrl(baseUrl.toString()+"/#/profile");
        merchantPreferences.setReturnUrl(baseUrl.toString()+"/finalize");
        merchantPreferences.setMaxFailAttempts("0");
        merchantPreferences.setAutoBillAmount("YES");
        merchantPreferences.setInitialFailAmountAction("CONTINUE");
        plan.setMerchantPreferences(merchantPreferences);

        APIContext apiContext = getApiContext();

        Plan createdPlan = plan.create(apiContext);

        Plan newPlan = new Plan();
        newPlan.setState("ACTIVE");

        // incorporate new plan in Patch object
        Patch patch = new Patch();
        patch.setOp("replace");
        patch.setPath("/");
        patch.setValue(newPlan);

        // wrap the Patch object with PatchRequest
        List<Patch> patchRequest = new ArrayList<>();
        patchRequest.add(patch);

        createdPlan.update(apiContext, patchRequest);
        return createdPlan;
    }

    @RequestMapping(value = "/getPlans", method = RequestMethod.GET)
    public List<Plan> getPlans() throws Exception {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("status", "ACTIVE");
        parameters.put("page_size", "10");
        parameters.put("page", "0");
        parameters.put("total_required", "yes");

        PlanList planList = Plan.list(getApiContext(), parameters);
        if (planList!=null) {
            return planList.getPlans();
        }
        return null;
    }

    private APIContext getApiContext() throws Exception {
        Map<String, String> sdkConfig = new HashMap<>();
        sdkConfig.put("mode", "sandbox");
        String accessToken = new OAuthTokenCredential("AVZqeNLvBcXZ7WJ-u7zMAQccnJcI6zW6z0rP2mziQtOOIKR82nXGFm8cHiYweYc-RHwudrtz4dBaXp_o", "ECrDsuaxHGP17UxXe544sEMH2oLD_J9Bu027TB4RfUTJ1Sj9yKafZA93GLY0O2odHsg5ZR9erJFM08T1", sdkConfig).getAccessToken();
        APIContext apiContext = new APIContext(accessToken);
        apiContext.setConfigurationMap(sdkConfig);
        return apiContext;
    }

    private String getCurrentDateInISO8601() {
        TimeZone tz = TimeZone.getTimeZone("Europe/Paris");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);
        return df.format(new Date());
    }

    @RequestMapping(value = "/subscription", method = RequestMethod.GET)
    public Test subscription(HttpServletRequest request) throws Exception {
        StringBuilder baseUrl = new StringBuilder();
        baseUrl
                .append(request.getScheme())
                .append("://")
                .append(request.getLocalName())
                .append(":")
                .append(request.getLocalPort())
                .append(request.getContextPath());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getDetails();

        Plan agreementPlan = new Plan();
        agreementPlan.setId("P-4HE985578R210570KB7UZYYY");
        Agreement agreement = new Agreement();
        agreement.setStartDate(getCurrentDateInISO8601());
        agreement.setName("rcbook agreement");
        agreement.setDescription("agreement for rcbook plan");
        agreement.setPlan(agreementPlan);
        MerchantPreferences merchantPreferences = new MerchantPreferences();
        merchantPreferences.setCancelUrl(baseUrl.toString()+"/#/profile");
        merchantPreferences.setReturnUrl(baseUrl.toString()+"/finalize?userId="+user.getId());
        merchantPreferences.setMaxFailAttempts("0");
        merchantPreferences.setAutoBillAmount("YES");
        merchantPreferences.setInitialFailAmountAction("CONTINUE");
        agreement.setOverrideMerchantPreferences(merchantPreferences);
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        agreement.setPayer(payer);
        Agreement createdAgreement = agreement.create(getApiContext());

        for (Links links: createdAgreement.getLinks()) {
            if (links.getMethod().equals("REDIRECT")) {
                Test test = new Test();
                test.setUrl(links.getHref());
                return test;
            }
        }

        return null;
    }

    @RequestMapping(value = "/finalize", method = RequestMethod.GET)
    public RedirectView finalizeSubscription(@RequestParam("token") String token, @RequestParam("userId") String userId) throws Exception {
        Agreement executedAgreement = Agreement.execute(getApiContext(), token);
        if (executedAgreement!=null) {
            User user = userService.getUserById(Long.valueOf(userId))
                    .orElseThrow(() -> new UsernameNotFoundException(String.format("User with id=%s was not found", userId)));
            user.setAgreementId(executedAgreement.getId());
            user.setAccount("PREMIUM");
            userService.update(user);
            return new RedirectView("#/profile");
        }
        return new RedirectView("#/error");
    }

    @RequestMapping(value = "/suspend", method = RequestMethod.GET)
    public ResponseEntity suspendSubscription(@RequestParam("userId") String userId) throws Exception {
        User user = userService.getUserById(Long.valueOf(userId))
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with id=%s was not found", userId)));
        Agreement agreement = Agreement.get(getApiContext(), user.getAgreementId());
        if (agreement!=null) {
            AgreementStateDescriptor stateDescriptor = new AgreementStateDescriptor();
            stateDescriptor.setNote(String.format("Cancel agreement id=%s for user=%s", agreement.getId(), userId));
            agreement.suspend(getApiContext(), stateDescriptor);
            user.setAccount("FREE");
            userService.update(user);
        }
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/reactivate", method = RequestMethod.GET)
    public ResponseEntity reactivateSubscription(@RequestParam("userId") String userId) throws Exception {
        User user = userService.getUserById(Long.valueOf(userId))
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with id=%s was not found", userId)));
        Agreement agreement = Agreement.get(getApiContext(), user.getAgreementId());
        if (agreement!=null) {
            AgreementStateDescriptor stateDescriptor = new AgreementStateDescriptor();
            stateDescriptor.setNote(String.format("Reactivate agreement id=%s for user=%s", agreement.getId(), userId));
            agreement.reActivate(getApiContext(), stateDescriptor);
            user.setAccount("PREMIUM");
            userService.update(user);
        }
        return ResponseEntity.ok().build();
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
        redirectUrls.setCancelUrl(baseUrl.toString()+"/#/profile");
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
        return new RedirectView("#/profile");
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
