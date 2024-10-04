package com.bankingsystem.models;

import java.util.Objects;
import java.util.UUID;

public class Person {
    private String personId;
    private String firstName;
    private String lastName;
    private String email;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String country;

    public Person(String firstName, String lastName, String email, String addressLine1, String addressLine2, String city, String country) {
        this.personId = "person-" + UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.country = country;
    }

    public String getPersonId() {
        return this.personId;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String newFirstName) {
        this.firstName = newFirstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String newLastName) {
        this.lastName = newLastName;
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

    @Override
    public String toString() {
        return "Person{" +
                "firstName=" + firstName + '\'' +
                "lastName=" + lastName + '\'' +
                "email=" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        // same object
        if (this == obj) return true;
        // Null or not the same class
        if (obj == null || getClass() != obj.getClass()) return false;

        Person person = (Person) obj;
        // Compare fields for logical equality
        return Objects.equals(personId, person.personId) &&
                Objects.equals(email, person.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId, email);
    }
}
