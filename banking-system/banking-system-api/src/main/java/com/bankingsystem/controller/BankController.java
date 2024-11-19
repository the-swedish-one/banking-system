package com.bankingsystem.controller;

import com.bankingsystem.mapper.ApiBankMapper;
import com.bankingsystem.model.ApiBank;
import com.bankingsystem.model.Bank;
import com.bankingsystem.service.BankService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bank")
public class BankController {

    private final BankService bankService;
    private final ApiBankMapper apiBankMapper;

    public BankController(BankService bankService, ApiBankMapper apiBankMapper) {
        this.bankService = bankService;
        this.apiBankMapper = apiBankMapper;
    }

    // Create a new bank
    @PostMapping
    public ResponseEntity<ApiBank> createBank(@RequestBody ApiBank apiBank) {
        Bank bank = apiBankMapper.toServiceModel(apiBank);
        Bank createdBank = bankService.createBank(bank.getBankName(), bank.getBic());
        ApiBank createdApiBank = apiBankMapper.toApiModel(createdBank);
        return new ResponseEntity<>(createdApiBank, HttpStatus.CREATED);
    }

    // Get bank by BIC
    @GetMapping("/{bic}")
    public ResponseEntity<ApiBank> getBankByBic(@PathVariable String bic) {
        Bank bank = bankService.getBankByBic(bic);
        ApiBank apiBank = apiBankMapper.toApiModel(bank);
        return new ResponseEntity<>(apiBank, HttpStatus.OK);
    }

    // Update bank
    @PutMapping("/{bic}")
    public ResponseEntity<ApiBank> updateBank(@PathVariable String bic, @RequestBody ApiBank apiBank) {
        // Ensure the BIC in the path matches the BIC in the payload if needed
        apiBank.setBic(bic);
        Bank bank = apiBankMapper.toServiceModel(apiBank);
        Bank updatedBank = bankService.updateBank(bank.getBic(), bank.getBankName());
        ApiBank updatedApiBank = apiBankMapper.toApiModel(updatedBank);
        return new ResponseEntity<>(updatedApiBank, HttpStatus.OK);
    }
}
