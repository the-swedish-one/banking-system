package com.bankingsystem.mapper;

import com.bankingsystem.model.ApiCurrencyConversion;
import com.domain.model.CurrencyConversion;
import org.springframework.stereotype.Component;

@Component
public class ApiCurrencyConversionMapper {

    public CurrencyConversion toServiceModel(ApiCurrencyConversion apiModel) {
        if (apiModel == null) return null;
        CurrencyConversion currencyConversion = new CurrencyConversion();
        currencyConversion.setId(apiModel.getId());
        currencyConversion.setLastUpdated(apiModel.getLastUpdated());
        currencyConversion.setExchangeRates(apiModel.getExchangeRates());
        return currencyConversion;
    }

    public ApiCurrencyConversion toApiModel(CurrencyConversion serviceModel) {
        if (serviceModel == null) return null;
        ApiCurrencyConversion apiCurrencyConversion = new ApiCurrencyConversion();
        apiCurrencyConversion.setId(serviceModel.getId());
        apiCurrencyConversion.setLastUpdated(serviceModel.getLastUpdated());
        apiCurrencyConversion.setExchangeRates(serviceModel.getExchangeRates());
        return apiCurrencyConversion;
    }
}

