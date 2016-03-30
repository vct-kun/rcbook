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
}
