package com.practice.MedGen;

public class Items {
    String generic, nonGeneric;
    int id;

    public Items(String generic, String nonGeneric, int id) {
        this.generic = generic;
        this.nonGeneric = nonGeneric;
        this.id = id;
    }

    public Items() {
    }

    public String getGeneric() {
        return generic;
    }


    public String getNonGeneric() {
        return nonGeneric;
    }

    public int getId() {
        return id;
    }
}
