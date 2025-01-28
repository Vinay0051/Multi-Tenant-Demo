package com.dcms.Multi_Tenancy_Demo.Controller;


import com.dcms.Multi_Tenancy_Demo.Exception.CollectionNotFoundException;
import com.dcms.Multi_Tenancy_Demo.Model.Account;
import com.dcms.Multi_Tenancy_Demo.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;


    @PostMapping("/create-bank/{bankName}")
    public String createBankCollection(@PathVariable String bankName){
        try{
            accountService.createNewBank(bankName);
            return "Bank Collection created";
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }

    }

    @PostMapping("/add-entry")
    public Account createAccount(@RequestHeader("bankName") String bankName,@RequestBody String customerName){
        try {
            Account account = new Account(customerName);
            return accountService.saveEntity(account, bankName);
        } catch(CollectionNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


}
