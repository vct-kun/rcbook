package com.rcbook.domain;

import java.util.List;

/**
 * Created by vctran on 11/03/2016.
 */
public class Race {

    private Long id;
    private String startDate;
    private String nbDriver;
    private List<User> joinedDriver;
    private Club raceClub;
    private String name;
    private String endDate;
    private String track;
    private String town;
    private String country;

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

    public List<User> getJoinedDriver() {
        return joinedDriver;
    }

    public void setJoinedDriver(List<User> joinedDriver) {
        this.joinedDriver = joinedDriver;
    }

    public Club getRaceClub() {
        return raceClub;
    }

    public void setRaceClub(Club raceClub) {
        this.raceClub = raceClub;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
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
}
