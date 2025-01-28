package com.dcms.Multi_Tenancy_Demo.Service;

import com.dcms.Multi_Tenancy_Demo.Exception.CollectionNotFoundException;
import com.dcms.Multi_Tenancy_Demo.Model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;



@Service
public class AccountService {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public AccountService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void createNewBank(String bankName){

        if (mongoTemplate.collectionExists(bankName)){
            throw new IllegalArgumentException("Bank already exists");
        }else{
            mongoTemplate.createCollection(bankName);
        }

    }

    public Account saveEntity(Account customerName, String bankName) {
        if (!mongoTemplate.collectionExists(bankName)){
            throw new CollectionNotFoundException(bankName + " not found");
        }
        return mongoTemplate.save(customerName, bankName);

    }

}
