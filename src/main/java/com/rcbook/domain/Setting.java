package com.rcbook.domain;

import javax.persistence.*;

/**
 * Created by vctran on 23/05/16.
 */
@Entity
@Table(name = "setting")
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "motor_id")
    private Motor motor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "esc_id")
    private Esc esc;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id")
    private Car car;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Motor getMotor() {
        return motor;
    }

    public void setMotor(Motor motor) {
        this.motor = motor;
    }

    public Esc getEsc() {
        return esc;
    }

    public void setEsc(Esc esc) {
        this.esc = esc;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}
