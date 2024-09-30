package com.bankingsystem.models;

public class Person {
    private String name;
    private String email;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String country;

    public Person(String name, String email, String addressLine1, String addressLine2, String city, String country) {
        this.name = name;
        this.email = email;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String newAddressLine1) {
        this.addressLine1 = newAddressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String newAddressLine2) {
        this.addressLine2 = newAddressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String newCity) {
        this.city = newCity;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String newCountry) {
        this.country = newCountry;
    }
}
