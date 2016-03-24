package com.rcbook.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

/**
 * Created by vctran on 11/03/2016.
 */
@Entity
@Table(name = "race")
public class Race {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "startDate")
    private String startDate;

    @Column(name = "nbDriver")
    private String nbDriver;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Driver> joinedDriver;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "club_id")
    private Club raceClub;

    @Column(name = "name")
    private String name;

    @Column(name = "endDate")
    private String endDate;

    @Column(name = "track")
    private String track;

    @Column(name = "town")
    private String town;

    @Column(name = "country")
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

    public List<Driver> getJoinedDriver() {
        return joinedDriver;
    }

    public void setJoinedDriver(List<Driver> joinedDriver) {
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
