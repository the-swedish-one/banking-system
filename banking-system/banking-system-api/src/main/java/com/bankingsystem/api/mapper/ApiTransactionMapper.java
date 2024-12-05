package com.bankingsystem.api.mapper;

import com.bankingsystem.api.model.ApiTransaction;
import com.bankingsystem.domain.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class ApiTransactionMapper {

    public Transaction toServiceModel(ApiTransaction apiModel) {
        if (apiModel == null) return null;
        Transaction model = new Transaction();
        model.setTransactionId(apiModel.getTransactionId());
        model.setAmount(apiModel.getAmount());
        model.setTimestamp(apiModel.getTimestamp());
        model.setToAccountIban(apiModel.getToAccountIban());
        model.setFromAccountIban(apiModel.getFromAccountIban());
        return model;
    }

    public ApiTransaction toApiModel(Transaction model) {
        if (model == null) return null;
        ApiTransaction apiModel = new ApiTransaction();
        apiModel.setTransactionId(model.getTransactionId());
        apiModel.setAmount(model.getAmount());
        apiModel.setTimestamp(model.getTimestamp());
        apiModel.setToAccountIban(model.getToAccountIban());
        apiModel.setFromAccountIban(model.getFromAccountIban());
        return apiModel;
    }
}
