package com.practice.MedGen;

public class Items {
    String generic, nonGeneric;

    public Items(String firstName, String lastName) {
        this.generic = firstName;
        this.nonGeneric = lastName;
    }

    public Items() {
    }

    public String getGeneric() {
        return generic;
    }

    public void setGeneric(String generic) {
        this.generic = generic;
    }

    public String getNonGeneric() {
        return nonGeneric;
    }

    public void setNonGeneric(String nonGeneric) {
        this.nonGeneric = nonGeneric;
    }
}
