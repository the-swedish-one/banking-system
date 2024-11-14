package com.bankingsystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "person_details")
@Data
@Getter
@Builder
@EqualsAndHashCode
public class PersonDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int personId;

    @Setter
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Setter
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Setter
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Setter
    @Column(name = "address_line1")
    private String addressLine1;

    @Setter
    @Column(name = "address_line2")
    private String addressLine2;

    @Setter
    @Column(name = "city")
    private String city;

    @Setter
    @Column(name = "country")
    private String country;

    public PersonDetailsEntity(String firstName, String lastName, String email, String addressLine1, String addressLine2, String city, String country) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.country = country;
    }

    public PersonDetailsEntity() {}

    public int getPersonId() {
        return this.personId;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }


}

