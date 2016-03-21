package com.rcbook.domain;

import java.util.List;

/**
 * Created by vctran on 18/03/16.
 */
public class Club {
    private Long id;

    private String name;

    private String town;

    private String country;

    private String url;

    private List<User> users;

    private List<User> waitingUsers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<User> getWaitingUsers() {
        return waitingUsers;
    }

    public void setWaitingUsers(List<User> waitingUsers) {
        this.waitingUsers = waitingUsers;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
