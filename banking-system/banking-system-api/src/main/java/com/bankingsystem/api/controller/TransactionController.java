package com.bankingsystem.api.controller;

import com.bankingsystem.api.mapper.ApiTransactionMapper;
import com.bankingsystem.api.model.ApiTransaction;
import com.bankingsystem.domain.model.Transaction;
import com.bankingsystem.domain.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final ApiTransactionMapper apiTransactionMapper;

    @Autowired
    public TransactionController(TransactionService transactionService, ApiTransactionMapper apiTransactionMapper) {
        this.transactionService = transactionService;
        this.apiTransactionMapper = apiTransactionMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiTransaction> getTransactionById(@PathVariable int id) {
        Transaction transaction = transactionService.getTransactionById(id);
        ApiTransaction apiTransaction = apiTransactionMapper.toApiModel(transaction);
        return new ResponseEntity<>(apiTransaction, HttpStatus.OK);
    }

    // Get all transactions for an IBAN
    @GetMapping("/iban/{iban}")
    public ResponseEntity<List<ApiTransaction>> getTransactionsByIban(@PathVariable String iban) {
        List<Transaction> transactions = transactionService.getTransactionsByIban(iban);
        List<ApiTransaction> apiTransactions = transactions.stream()
                .map(apiTransactionMapper::toApiModel)
                .collect(Collectors.toList());
        return new ResponseEntity<>(apiTransactions, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ApiTransaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        List<ApiTransaction> apiTransactions = transactions.stream()
                .map(apiTransactionMapper::toApiModel)
                .collect(Collectors.toList());
        return new ResponseEntity<>(apiTransactions, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable int id) {
        transactionService.deleteTransaction(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
