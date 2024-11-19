package com.bankingsystem.mapper;

import com.bankingsystem.model.TransferTransaction;
import org.springframework.stereotype.Component;

@Component
public class ApiTransactionMapper {

    public TransferTransaction toServiceModel(ApiTransferTransaction apiModel) {
        if (apiModel == null) return null;
        TransferTransaction model = new TransferTransaction();
        model.setTransactionId(apiModel.getTransactionId());
        model.setAmount(apiModel.getAmount());
        model.setTimestamp(apiModel.getTimestamp());
        model.setToAccountId(apiModel.getToAccountId());
        model.setFromAccountId(apiModel.getFromAccountId());
        return model;
    }

    public ApiTransferTransaction toApiModel(TransferTransaction model) {
        if (model == null) return null;
        ApiTransferTransaction apiModel = new ApiTransferTransaction();
        apiModel.setTransactionId(model.getTransactionId());
        apiModel.setAmount(model.getAmount());
        apiModel.setTimestamp(model.getTimestamp());
        apiModel.setToAccountId(model.getToAccountId());
        apiModel.setFromAccountId(model.getFromAccountId());
        return apiModel;
    }
}
