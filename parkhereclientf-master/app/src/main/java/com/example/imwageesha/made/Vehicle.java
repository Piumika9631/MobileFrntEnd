package com.example.imwageesha.made;

import java.io.Serializable;

public class Vehicle implements Serializable {
    String name;
    String number;
    String type;

    public Vehicle() {
    }

    public Vehicle(String name, String number, String type) {
        this.name = name;
        this.number = number;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
