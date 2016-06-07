package com.rcbook.controller;

import com.paypal.api.payments.*;
import com.paypal.api.payments.Currency;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.svcs.services.AdaptivePaymentsService;
import com.paypal.svcs.types.ap.PayRequest;
import com.paypal.svcs.types.ap.PayResponse;
import com.paypal.svcs.types.ap.Receiver;
import com.paypal.svcs.types.ap.ReceiverList;
import com.paypal.svcs.types.common.RequestEnvelope;
import com.rcbook.domain.Driver;
import com.rcbook.domain.Race;
import com.rcbook.domain.User;
import com.rcbook.service.user.DriverService;
import com.rcbook.service.user.RaceService;
import com.rcbook.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private RaceService raceService;

    @Autowired
    private DriverService driverService;

    @Value("${application.domain.name}")
    private String domain;

    @RequestMapping(value = "/createPlan", method = RequestMethod.GET)
    public Plan createPlan(HttpServletRequest request) throws Exception {
        StringBuilder baseUrl = getBaseUrl(request);

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
    public UrlToRedirect subscription(HttpServletRequest request) throws Exception {
        StringBuilder baseUrl = getBaseUrl(request);
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
                UrlToRedirect urlToRedirect = new UrlToRedirect();
                urlToRedirect.setUrl(links.getHref());
                return urlToRedirect;
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

    @RequestMapping(value = "/payRace", method = RequestMethod.GET)
    public UrlToRedirect payRace(HttpServletRequest request, @RequestParam("driverId") String driverId, @RequestParam("raceId") String raceId) throws Exception {
        StringBuilder baseUrl = getBaseUrl(request);

        Race race = raceService.getRaceById(Long.valueOf(raceId));
        String raceOwnerEmail = race.getRaceClub().getOwner().getEmail();

        PayRequest payRequest = new PayRequest();
        List<Receiver> receivers = new ArrayList<>();
        Receiver receiver = new Receiver();
        receiver.setAmount(Double.valueOf(race.getPrice()));
        receiver.setEmail("tran_vcharles-facilitator@yahoo.fr");
        receivers.add(receiver);
        ReceiverList receiverList = new ReceiverList(receivers);

        payRequest.setReceiverList(receiverList);

        RequestEnvelope requestEnvelope = new RequestEnvelope("fr_FR");
        payRequest.setRequestEnvelope(requestEnvelope);
        payRequest.setActionType("PAY");
        payRequest.setCancelUrl(baseUrl.toString()+"/#/profile");
        payRequest.setReturnUrl(baseUrl.toString()+"/confirmRacePayment?driverId="+driverId+"&raceId="+raceId);
        payRequest.setCurrencyCode("EUR");
        payRequest.setIpnNotificationUrl(baseUrl.toString());

        Map<String, String> sdkConfig = new HashMap<>();
        sdkConfig.put("mode", "sandbox");
        sdkConfig.put("acct1.UserName", "tran_vcharles-facilitator_api1.yahoo.fr");
        sdkConfig.put("acct1.Password", "RT85TBM2E63T4UG2");
        sdkConfig.put("acct1.Signature", "AfXSUWQ.gOQ0Fd7msGpkmoWillSfAGSMWeVoVSZbsQU0pEHcqEz33DA6");
        sdkConfig.put("acct1.AppId", "APP-80W284485P519543T");

        AdaptivePaymentsService adaptivePaymentsService = new AdaptivePaymentsService(sdkConfig);
        PayResponse payResponse = adaptivePaymentsService.pay(payRequest);
        UrlToRedirect urlToRedirect = new UrlToRedirect();
        urlToRedirect.setUrl("https://www.sandbox.paypal.com/webscr?cmd=_ap-payment&paykey="+payResponse.getPayKey());
        return urlToRedirect;
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

    @RequestMapping(value = "/confirmRacePayment", method = RequestMethod.GET)
    public RedirectView confirmRacePayment(@RequestParam("driverId") String driverId, @RequestParam("raceId") String raceId) throws Exception {
        Driver driver = driverService.findDriverById(Long.valueOf(driverId));
        driver.setJoiningStatus("CONFIRMED");
        driverService.updateDriver(driver);
        //Notify race owner payment
        return new RedirectView("#/paymentdone");
    }

    class UrlToRedirect {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
