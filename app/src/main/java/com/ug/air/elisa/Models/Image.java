package com.ug.air.elisa.Models;

public class Image {

    String symptom;
    String image_url;

    public Image(String symptom, String image_url) {
        this.symptom = symptom;
        this.image_url = image_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getSymptom() {
        return symptom;
    }
}
