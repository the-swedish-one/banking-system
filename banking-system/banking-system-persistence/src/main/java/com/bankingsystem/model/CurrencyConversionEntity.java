package com.bankingsystem.model;

import com.bankingsystem.enums.CurrencyCode;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Entity
@Table(name = "currency_conversion")
@Data
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class CurrencyConversionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_updated")
    private Date lastUpdated;

    @ElementCollection
    @CollectionTable(name = "exchange_rates", joinColumns = @JoinColumn(name = "conversion_id"))
    @MapKeyColumn(name = "currency_code")
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "rate")
    private Map<CurrencyCode, Double> exchangeRates;

    public CurrencyConversionEntity(Map<CurrencyCode, Double> exchangeRates) {
        this.exchangeRates = exchangeRates;
        this.lastUpdated = new Date();
    }
}
