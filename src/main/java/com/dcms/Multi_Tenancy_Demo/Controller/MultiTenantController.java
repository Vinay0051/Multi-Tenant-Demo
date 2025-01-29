package com.dcms.Multi_Tenancy_Demo.Controller;


import com.dcms.Multi_Tenancy_Demo.Exception.CollectionOrDbNotFoundException;
import com.dcms.Multi_Tenancy_Demo.Model.DebitCard;
import com.dcms.Multi_Tenancy_Demo.Service.MultiTenantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/dcms")

public class MultiTenantController {

    private final MultiTenantService multiTenantService;


    private static final Logger logger = LoggerFactory.getLogger("MultiTenantController");

    public MultiTenantController(MultiTenantService multiTenantService) {
        this.multiTenantService = multiTenantService;
    }


    @PostMapping("/onboard-tenant")
    public ResponseEntity<Map<String, String>> createDatabase(@RequestHeader("bankName") String bankName){
        try {
            logger.info("Received request to create database for bank: {}", bankName);
            multiTenantService.createNewDatabase(bankName);
            Map<String, String> response =  new HashMap<>();
            response.put("message", "Database created and registered in MasterDB: " + bankName);
            logger.info("Database created successfully: {}", bankName);
            return ResponseEntity.ok(response);
        }catch (IllegalArgumentException e){
            logger.error("Error creating database for bank: {}", bankName, e);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
    }

    @PostMapping("/debit-entry")
    public ResponseEntity<Map<String, String>> addDebitEntry(@RequestHeader("bankName") String bankName, @RequestBody Map<String, String> request){
        try {
            logger.info("Received request to add debit entry for bank: {}", bankName);
            Map<String, String> response =  new HashMap<>();
            DebitCard debitCard = new DebitCard(request.get("card_network"), request.get("cvv"));
            multiTenantService.saveDebitCardEntry(bankName, debitCard);
            response.put("message", "Debit entry created with network: " + debitCard.getCardNetwork());
            logger.info("Debit entry created for bank: {} with network: {}", bankName, debitCard.getCardNetwork());
            return ResponseEntity.ok(response);
        }
        catch (CollectionOrDbNotFoundException e){
            logger.error("Error adding debit entry for bank: {}", bankName, e);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
    }
}
