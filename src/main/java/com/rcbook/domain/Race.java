package com.rcbook.domain;

import javax.persistence.*;
import java.util.Set;

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

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "nb_driver")
    private String nbDriver;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Driver> joinedDriver;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "club_id")
    private Club raceClub;

    @Column(name = "name")
    private String name;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "track")
    private String track;

    @Column(name = "town")
    private String town;

    @Column(name = "country")
    private String country;

    @Column(name = "closed")
    private boolean closed;

    @Column(name = "have_fees")
    private boolean haveFees;

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

    public Set<Driver> getJoinedDriver() {
        return joinedDriver;
    }

    public void setJoinedDriver(Set<Driver> joinedDriver) {
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

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public boolean isHaveFees() {
        return haveFees;
    }

    public void setHaveFees(boolean haveFees) {
        this.haveFees = haveFees;
    }
}
