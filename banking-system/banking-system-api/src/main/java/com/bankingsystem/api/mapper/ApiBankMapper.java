package com.bankingsystem.api.mapper;

import com.bankingsystem.api.model.ApiBank;
import com.bankingsystem.domain.model.Bank;
import org.springframework.stereotype.Component;

@Component
public class ApiBankMapper {

    public Bank toServiceModel(ApiBank apiModel) {
        if (apiModel == null) return null;
        Bank bank = new Bank();
        bank.setId(apiModel.getId());
        bank.setBankName(apiModel.getBankName());
        bank.setBic(apiModel.getBic());
        bank.setCollectedInterest(apiModel.getCollectedInterest());
        return bank;
    }

    public ApiBank toApiModel(Bank serviceModel) {
        if (serviceModel == null) return null;
        ApiBank apiBank = new ApiBank();
        apiBank.setId(serviceModel.getId());
        apiBank.setBankName(serviceModel.getBankName());
        apiBank.setBic(serviceModel.getBic());
        apiBank.setCollectedInterest(serviceModel.getCollectedInterest());
        return apiBank;
    }
}
