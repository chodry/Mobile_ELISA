package com.ug.air.elisa.Models;

public class Name {

    String sample;
    String sample_name;

    public Name() {
    }

    public Name(String sample, String sample_name) {
        this.sample = sample;
        this.sample_name = sample_name;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public String getSample_name() {
        return sample_name;
    }

    public void setSample_name(String sample_name) {
        this.sample_name = sample_name;
    }
}
