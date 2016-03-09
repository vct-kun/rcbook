package com.example;

import java.util.List;

/**
 * Created by vctran on 09/03/16.
 */
public class Brand {
    private String name;

    private List<Chassis> chassisList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Chassis> getChassisList() {
        return chassisList;
    }

    public void setChassisList(List<Chassis> chassisList) {
        this.chassisList = chassisList;
    }
}
