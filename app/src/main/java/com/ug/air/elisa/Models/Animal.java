package com.ug.air.elisa.Models;

public class Animal {

    String type;
    String total;

    public Animal() {
    }

    public Animal(String type, String total) {
        this.type = type;
        this.total = total;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
