package com.ug.air.elisa.Models;

public class Ill {

    String suffer;
    String treatment;

    public Ill() {
    }

    public Ill(String suffer, String treatment) {
        this.suffer = suffer;
        this.treatment = treatment;
    }

    public String getSuffer() {
        return suffer;
    }

    public void setSuffer(String suffer) {
        this.suffer = suffer;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }
}
