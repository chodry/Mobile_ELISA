package com.ug.air.elisa.Models;

public class Form {

    String name, location, date, animal, filename;

    public Form(String name, String location, String date, String animal, String filename) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.animal = animal;
        this.filename = filename;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getAnimal() {
        return animal;
    }

    public String getFilename() {
        return filename;
    }
}
