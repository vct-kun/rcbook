package com.rcbook.controller;

import com.rcbook.domain.User;
import org.springframework.context.ApplicationEvent;

/**
 * Created by vctran on 09/06/16.
 */
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private final User user;
    private final String url;

    public OnRegistrationCompleteEvent(User user, String url) {
        super(user);
        this.user = user;
        this.url = url;
    }

    public User getUser() {
        return user;
    }

    public String getUrl() {
        return url;
    }
}
