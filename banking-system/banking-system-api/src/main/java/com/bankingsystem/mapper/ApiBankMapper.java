package com.bankingsystem.mapper;

import com.bankingsystem.model.ApiBank;
import com.domain.model.Bank;
import org.springframework.stereotype.Component;

@Component
public class ApiBankMapper {

    public Bank toServiceModel(ApiBank apiModel) {
        if (apiModel == null) return null;
        Bank bank = new Bank();
        bank.setBankName(apiModel.getBankName());
        bank.setBic(apiModel.getBic());
        return bank;
    }

    public ApiBank toApiModel(Bank serviceModel) {
        if (serviceModel == null) return null;
        ApiBank apiBank = new ApiBank();
        apiBank.setBankName(serviceModel.getBankName());
        apiBank.setBic(serviceModel.getBic());
        return apiBank;
    }
}
