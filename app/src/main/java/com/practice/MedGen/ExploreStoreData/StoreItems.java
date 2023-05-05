package com.practice.MedGen.ExploreStoreData;

public class StoreItems {
    String address, contact_person, district, state, store_code, phone_number, pin_code;

    public StoreItems() {
    }

    public StoreItems(String address, String contact_person, String district, String state, String store_code, String phone_number, String pin_code) {
        this.address = address;
        this.contact_person = contact_person;
        this.district = district;
        this.state = state;
        this.store_code = store_code;
        this.phone_number = phone_number;
        this.pin_code = pin_code;
    }

    public String getAddress() {
        return address;
    }

    public String getContact_person() {
        return contact_person;
    }

    public String getDistrict() {
        return district;
    }

    public String getState() {
        return state;
    }

    public String getStore_code() {
        return store_code;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getPin_code() {
        return pin_code;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setStore_code(String store_code) {
        this.store_code = store_code;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setPin_code(String pin_code) {
        this.pin_code = pin_code;
    }
}
