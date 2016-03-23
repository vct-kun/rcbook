package com.rcbook.domain;

import javax.persistence.*;

/**
 * Created by vctran on 08/03/16.
 */
@Entity
@Table(name = "car")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @OneToOne(cascade = CascadeType.DETACH, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "id", nullable = false)
    private Chassis chassis;

//    @ManyToOne(optional = false)
//    @JoinColumn(name = "user", referencedColumnName = "id")
//    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Chassis getChassis() {
        return chassis;
    }

    public void setChassis(Chassis chassis) {
        this.chassis = chassis;
    }

//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
}
