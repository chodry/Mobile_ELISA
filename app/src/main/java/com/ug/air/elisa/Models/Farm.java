package com.ug.air.elisa.Models;

public class Farm {

    String name, location, date, animal, filename, option;

    public Farm(String name, String location, String date, String animal, String filename, String option) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.animal = animal;
        this.filename = filename;
        this.option = option;
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

    public String getOption() {
        return option;
    }
}
