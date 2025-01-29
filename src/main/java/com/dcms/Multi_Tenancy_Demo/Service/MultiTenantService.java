package com.dcms.Multi_Tenancy_Demo.Service;

import com.dcms.Multi_Tenancy_Demo.Exception.CollectionOrDbNotFoundException;
import com.dcms.Multi_Tenancy_Demo.Model.BankDetails;
import com.dcms.Multi_Tenancy_Demo.Model.DebitCard;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;


@Service
public class MultiTenantService {

    private final MongoClient mongoClient;
    private final MongoTemplate mongoTemplate;
    private static final String MASTER_DB = "DebitMasterDB";
    private static final String MASTER_COLLECTION = "BankDetails";

    @Autowired
    public MultiTenantService(MongoClient mongoClient, MongoTemplate mongoTemplate) {
        this.mongoClient = mongoClient;
        this.mongoTemplate = mongoTemplate;
    }

//    public void createNewDatabase(String bankName){
//
//        boolean databaseExists = mongoClient.listDatabaseNames().into(new ArrayList<>()).contains(bankName);
//        if(databaseExists){
//            throw new IllegalArgumentException("Database already exists");
//        }
//
//        MongoDatabase newDatabase = mongoClient.getDatabase(bankName+"_debitService");
//        newDatabase.createCollection("debitCardInformation");
//
//        BankDetails bankDetails = new BankDetails(bankName, bankName);
//        mongoTemplate.save(bankDetails, MASTER_DB);
//    }

    public void createNewDatabase(String bankName) {
        boolean databaseExists = mongoClient.listDatabaseNames().into(new ArrayList<>()).contains(bankName + "_debitService");
        if (databaseExists) {
            throw new IllegalArgumentException("Database already exists");
        }

        String databaseName = bankName + "_debitService";
        MongoDatabase newDatabase = mongoClient.getDatabase(databaseName);
        newDatabase.createCollection("debitCardInformation");

        BankDetails bankDetails = new BankDetails(bankName, databaseName);
        mongoTemplate.save(bankDetails, MASTER_DB);
    }

    public void saveDebitCardEntry(String bankName, DebitCard debitCard) {
        boolean databaseExists = mongoClient.listDatabaseNames().into(new ArrayList<>()).contains(bankName+"_debitService");

        if(databaseExists){
            MongoDatabase newDatabase = mongoClient.getDatabase(bankName+"_debitService");
          Document doc = new Document()
                    .append("card_network", debitCard.getCard_network())
                    .append("cvv", debitCard.getCvv());
            newDatabase.getCollection("debitCardInformation").insertOne(doc);
        }else{
            throw new CollectionOrDbNotFoundException(bankName + " not found");
        }
    }

}
