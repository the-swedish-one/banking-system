package com.bankingsystem.mapper;

import com.bankingsystem.model.ApiCheckingAccount;
import com.bankingsystem.model.CheckingAccount;
import org.springframework.stereotype.Component;

@Component
public class ApiCheckingAccountMapper {

    private final ApiUserMapper apiUserMapper;

    public ApiCheckingAccountMapper(ApiUserMapper apiUserMapper) {
        this.apiUserMapper = apiUserMapper;
    }

    public CheckingAccount toServiceModel(ApiCheckingAccount apiModel) {
        if (apiModel == null) return null;
        CheckingAccount model = new CheckingAccount();
        model.setAccountId(apiModel.getAccountId());
        model.setIban(apiModel.getIban());
        model.setAccountName(apiModel.getAccountName());
        model.setOwner(apiUserMapper.toServiceModel(apiModel.getOwner()));
        model.setBalance(apiModel.getBalance());
        model.setCurrency(apiModel.getCurrency());
        model.setOverdraftLimit(apiModel.getOverdraftLimit());
        return model;
    }

    public ApiCheckingAccount toApiModel(CheckingAccount serviceModel) {
        if (serviceModel == null) return null;
        ApiCheckingAccount apiModel = new ApiCheckingAccount();
        apiModel.setAccountId(serviceModel.getAccountId());
        apiModel.setIban(serviceModel.getIban());
        apiModel.setAccountName(serviceModel.getAccountName());
        apiModel.setOwner(apiUserMapper.toApiModel(serviceModel.getOwner()));
        apiModel.setBalance(serviceModel.getBalance());
        apiModel.setCurrency(serviceModel.getCurrency());
        apiModel.setOverdraftLimit(serviceModel.getOverdraftLimit());
        return apiModel;
    }
}
