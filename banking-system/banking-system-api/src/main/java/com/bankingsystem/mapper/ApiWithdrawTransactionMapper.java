package com.bankingsystem.mapper;

import com.bankingsystem.model.*;
import org.springframework.stereotype.Component;

@Component
public class ApiWithdrawTransactionMapper {

    public WithdrawTransaction toServiceModel(ApiWithdrawTransaction apiModel) {
        if (apiModel == null) return null;
        WithdrawTransaction model = new WithdrawTransaction();
        model.setTransactionId(apiModel.getTransactionId());
        model.setAmount(apiModel.getAmount());
        model.setTimestamp(apiModel.getTimestamp());
        model.setFromAccountId(apiModel.getFromAccountId());
        return model;
    }

    public ApiWithdrawTransaction toApiModel(WithdrawTransaction model) {
        if (model == null) return null;
        ApiWithdrawTransaction apiModel = new ApiWithdrawTransaction();
        apiModel.setTransactionId(model.getTransactionId());
        apiModel.setAmount(model.getAmount());
        apiModel.setTimestamp(model.getTimestamp());
        apiModel.setFromAccountId(model.getFromAccountId());
        return apiModel;
    }
}
