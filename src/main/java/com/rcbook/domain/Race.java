package com.rcbook.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vctran on 11/03/2016.
 */
public class Race {
    private Long id;

    private String startDate;

    private String nbDriver;

    private User joinedDriver;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getNbDriver() {
        return nbDriver;
    }

    public void setNbDriver(String nbDriver) {
        this.nbDriver = nbDriver;
    }

    public User getJoinedDriver() {
        return joinedDriver;
    }

    public void setJoinedDriver(User joinedDriver) {
        this.joinedDriver = joinedDriver;
    }
}
