package com.dcms.Multi_Tenancy_Demo.Service;

import com.dcms.Multi_Tenancy_Demo.Exception.CollectionOrDbNotFoundException;
import com.dcms.Multi_Tenancy_Demo.Model.DebitCard;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class MultiTenantService {

    private final MongoClient mongoClient;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public MultiTenantService(MongoClient mongoClient, MongoTemplate mongoTemplate) {
        this.mongoClient = mongoClient;
        this.mongoTemplate = mongoTemplate;
    }

    public void createNewDatabase(String bankName){

        boolean databaseExists = mongoClient.listDatabaseNames().into(new ArrayList<>()).contains(bankName);
        if(databaseExists){
            throw new IllegalArgumentException("Database already exists");
        }

        MongoDatabase newDatabase = mongoClient.getDatabase(bankName);
        newDatabase.createCollection("DebitService");
    }

    public void saveDebitCardEntry(String bankName, DebitCard debitCard) {
        boolean databaseExists = mongoClient.listDatabaseNames().into(new ArrayList<>()).contains(bankName);
        if(databaseExists){
            MongoDatabase newDatabase = mongoClient.getDatabase(bankName);
            Document doc = new Document("card_id", debitCard.getCard_id())
                    .append("card_network", debitCard.getCard_network());
            newDatabase.getCollection("debitCardInformation").insertOne(doc);
        }else{
            throw new CollectionOrDbNotFoundException(bankName + " not found");
        }
    }

}
