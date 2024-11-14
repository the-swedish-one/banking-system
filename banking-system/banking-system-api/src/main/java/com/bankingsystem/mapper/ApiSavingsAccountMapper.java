package com.bankingsystem.mapper;

import com.bankingsystem.model.ApiSavingsAccount;
import com.bankingsystem.model.SavingsAccount;
import org.springframework.stereotype.Component;

@Component
public class ApiSavingsAccountMapper {

    private final ApiUserMapper apiUserMapper;

    public ApiSavingsAccountMapper(ApiUserMapper apiUserMapper) {
        this.apiUserMapper = apiUserMapper;
    }

    public SavingsAccount toServiceModel(ApiSavingsAccount apiModel) {
        if (apiModel == null) return null;
        SavingsAccount model = new SavingsAccount();
        model.setAccountId(apiModel.getAccountId());
        model.setIban(apiModel.getIban());
        model.setAccountName(apiModel.getAccountName());
        model.setOwner(apiUserMapper.toServiceModel(apiModel.getOwner()));
        model.setBalance(apiModel.getBalance());
        model.setCurrency(apiModel.getCurrency());
        model.setInterestRatePercentage(apiModel.getInterestRatePercentage());
        return model;
    }

    public ApiSavingsAccount toApiModel(SavingsAccount model) {
        if (model == null) return null;
        ApiSavingsAccount apiModel = new ApiSavingsAccount();
        apiModel.setAccountId(model.getAccountId());
        apiModel.setIban(model.getIban());
        apiModel.setAccountName(model.getAccountName());
        apiModel.setOwner(apiUserMapper.toApiModel(model.getOwner()));
        apiModel.setBalance(model.getBalance());
        apiModel.setCurrency(model.getCurrency());
        apiModel.setInterestRatePercentage(model.getInterestRatePercentage());
        return apiModel;
    }
}
