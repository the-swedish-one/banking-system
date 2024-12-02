package com.bankingsystem.domain.mapper;

import com.bankingsystem.domain.model.CurrencyConversion;
import com.bankingsystem.persistence.model.CurrencyConversionEntity;
import org.springframework.stereotype.Component;

@Component
public class CurrencyConversionMapper {

    public CurrencyConversion toModel(CurrencyConversionEntity entity) {
        if (entity == null) return null;
        CurrencyConversion currencyConversion = new CurrencyConversion();
        currencyConversion.setId(entity.getId());
        currencyConversion.setLastUpdated(entity.getLastUpdated());
        currencyConversion.setExchangeRates(entity.getExchangeRates());
        return currencyConversion;
    }

    public CurrencyConversionEntity toEntity(CurrencyConversion model) {
        if (model == null) return null;
        CurrencyConversionEntity entity = new CurrencyConversionEntity();
        entity.setExchangeRates(model.getExchangeRates());
        return entity;
    }
}
