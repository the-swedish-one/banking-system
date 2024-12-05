package com.bankingsystem.persistence.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bank_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "person_id", referencedColumnName = "personId")
    private PersonDetailsEntity person;

    public UserEntity(PersonDetailsEntity person) {
        this.person = person;
    }
}
