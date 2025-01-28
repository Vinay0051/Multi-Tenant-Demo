package com.dcms.Multi_Tenancy_Demo.Controller;


import com.dcms.Multi_Tenancy_Demo.Exception.CollectionOrDbNotFoundException;
import com.dcms.Multi_Tenancy_Demo.Model.Account;
import com.dcms.Multi_Tenancy_Demo.Model.DebitCard;
import com.dcms.Multi_Tenancy_Demo.Service.MultiTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class MultiTenantController {

    @Autowired
    private MultiTenantService multiTenantService;


    @PostMapping("/onboard-tenant")
    public ResponseEntity<Map<String, String>> createDatabase(@RequestHeader("bankName") String bankName){
        try {
            Map<String, String> response =  new HashMap<>();
            multiTenantService.createNewDatabase(bankName);
            response.put("message", "Database created with name: " + bankName);
            return ResponseEntity.ok(response);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
    }

    @PostMapping("/add-debit-entry")
    public ResponseEntity<Map<String, String>> addDebitEntry(@RequestHeader("bankName") String bankName, @RequestBody Map<String, String> request){
        try {
            Map<String, String> response =  new HashMap<>();
            DebitCard debitCard = new DebitCard(request.get("card_network"));
            multiTenantService.saveDebitCardEntry(bankName, debitCard);
            response.put("message", "Debit entry created with network: " + debitCard.getCardNetwork());
            return ResponseEntity.ok(response);
        }
        catch (CollectionOrDbNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
    }

    @PostMapping("/add-account-entry")
    public ResponseEntity<Map<String, String>> addAccountEntry(@RequestHeader("bankName") String bankName, @RequestBody Account account){
        try {
            Map<String, String> response =  new HashMap<>();
            multiTenantService.saveAccountEntry(bankName, account);
            response.put("message", "Account created for name "+account.getAccount_holder_name());
            return ResponseEntity.ok(response);
        }catch (CollectionOrDbNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
    }
}
