package com.bankingsystem.mapper;

import com.bankingsystem.model.ApiJointCheckingAccount;
import com.bankingsystem.model.JointCheckingAccount;
import org.springframework.stereotype.Component;

@Component
public class ApiJointCheckingAccountMapper {

    private final ApiUserMapper apiUserMapper;

    public ApiJointCheckingAccountMapper(ApiUserMapper apiUserMapper) {
        this.apiUserMapper = apiUserMapper;
    }

    public JointCheckingAccount toServiceModel(ApiJointCheckingAccount apiModel) {
        if (apiModel == null) return null;
        JointCheckingAccount model = new JointCheckingAccount();
        model.setAccountId(apiModel.getAccountId());
        model.setIban(apiModel.getIban());
        model.setAccountName(apiModel.getAccountName());
        model.setOwner(apiUserMapper.toServiceModel(apiModel.getOwner()));
        model.setBalance(apiModel.getBalance());
        model.setCurrency(apiModel.getCurrency());
        model.setOverdraftLimit(apiModel.getOverdraftLimit());
        model.setSecondOwner(apiUserMapper.toServiceModel(apiModel.getSecondOwner()));
        return model;
    }

    public ApiJointCheckingAccount toApiModel(JointCheckingAccount model) {
        if (model == null) return null;
        ApiJointCheckingAccount apiModel = new ApiJointCheckingAccount();
        apiModel.setAccountId(model.getAccountId());
        apiModel.setIban(model.getIban());
        apiModel.setAccountName(model.getAccountName());
        apiModel.setOwner(apiUserMapper.toApiModel(model.getOwner()));
        apiModel.setBalance(model.getBalance());
        apiModel.setCurrency(model.getCurrency());
        apiModel.setOverdraftLimit(model.getOverdraftLimit());
        apiModel.setSecondOwner(apiUserMapper.toApiModel(model.getSecondOwner()));
        return apiModel;
    }
}
