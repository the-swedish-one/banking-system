package com.bankingsystem.mapper;

import com.bankingsystem.model.ApiDepositTransaction;
import com.bankingsystem.model.DepositTransaction;
import org.springframework.stereotype.Component;

@Component
public class ApiDepositTransactionMapper {

    public DepositTransaction toServiceModel(ApiDepositTransaction apiModel) {
        if (apiModel == null) return null;
        DepositTransaction model = new DepositTransaction();
        model.setTransactionId(apiModel.getTransactionId());
        model.setAmount(apiModel.getAmount());
        model.setTimestamp(apiModel.getTimestamp());
        model.setToAccountId(apiModel.getToAccountId());
        return model;
    }

    public ApiDepositTransaction toApiModel(DepositTransaction model) {
        if (model == null) return null;
        ApiDepositTransaction apiModel = new ApiDepositTransaction();
        apiModel.setTransactionId(model.getTransactionId());
        apiModel.setAmount(model.getAmount());
        apiModel.setTimestamp(model.getTimestamp());
        apiModel.setToAccountId(model.getToAccountId());
        return apiModel;
    }
}
