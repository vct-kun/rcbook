package com.rcbook.controller;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.*;
import com.rcbook.domain.User;
import com.rcbook.domain.VerificationToken;
import com.rcbook.service.user.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by vctran on 09/06/16.
 */
@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private TokenService tokenService;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, user);
        tokenService.save(verificationToken);

        String TO = "CTranVan.prestataire@voyages-sncf.com";
        String FROM = "tran.vcharles@gmail.com";
        String SUBJECT = "Registration Confirmation";
        String confirmationUrl = event.getUrl() + "/registrationConfirm?token=" + token;
        String BODY = "Click here: " + confirmationUrl;
        Destination destination = new Destination().withToAddresses(new String[]{TO});
        Content subject = new Content().withData(SUBJECT);
        Content textBody = new Content().withData(BODY);
        Body body = new Body().withText(textBody);

        Message message = new Message().withSubject(subject).withBody(body);

        SendEmailRequest request = new SendEmailRequest().withSource(FROM).withDestination(destination).withMessage(message);

        AWSCredentials credentials = new ProfileCredentialsProvider().getCredentials();

        AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(credentials);

        Region region = Region.getRegion(Regions.EU_WEST_1);
        client.setRegion(region);

        client.sendEmail(request);
    }
}
