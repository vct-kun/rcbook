package com.rcbook.domain;

import javax.persistence.*;

/**
 * Created by vctran on 24/03/16.
 */
@Entity
@Table(name = "driver")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id")
    private Car car;

    @Column(name = "rank")
    private String rank;

    @Column(name = "total_time")
    private String totalTime;

    @Column(name = "best_lap")
    private String bestLap;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "setting_id")
    private Setting setting;

    @Column(name = "joining_status")
    private String joiningStatus;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "race_id")
    private Race race;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getBestLap() {
        return bestLap;
    }

    public void setBestLap(String bestLap) {
        this.bestLap = bestLap;
    }

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    public String getJoiningStatus() {
        return joiningStatus;
    }

    public void setJoiningStatus(String joiningStatus) {
        this.joiningStatus = joiningStatus;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }
}
