package com.dcms.Multi_Tenancy_Demo.Service;

import com.dcms.Multi_Tenancy_Demo.Exception.CollectionOrDbNotFoundException;
import com.dcms.Multi_Tenancy_Demo.Model.BankDetails;
import com.dcms.Multi_Tenancy_Demo.Model.DebitCard;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import jakarta.annotation.PostConstruct;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@Service
public class MultiTenantService {

    private static final Logger logger = LoggerFactory.getLogger(MultiTenantService.class);

    private final MongoClient mongoClient;
    private final MongoTemplate mongoTemplate;
    private static final String MASTER_DB = "DebitServiceMaster";
//    private static final String MASTER_COLLECTION = "BankDetails";

    private final Map<String, String> tenantDatabaseCache = new HashMap<>();

    @Autowired
    public MultiTenantService(MongoClient mongoClient, MongoTemplate mongoTemplate) {
        this.mongoClient = mongoClient;
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void loadExistingDatabase() {
        logger.info("Loading existing tenant databases...");
        for (String dbName : mongoClient.listDatabaseNames()) {
            if (dbName.endsWith("_debitService")) {
                String bankName = dbName.replace("_debitService", "");
                tenantDatabaseCache.put(bankName, dbName);
            }
        }
        logger.info("Total tenants loaded: {}", tenantDatabaseCache.size());
        applyIndexesToAllDatabases();
//        System.out.println(tenantDatabaseCache.entrySet());
    }

    public void createNewDatabase(String bankName) {
//        boolean databaseExists = mongoClient.listDatabaseNames().into(new ArrayList<>()).contains(bankName + "_debitService");
//        if (databaseExists) {
//            throw new IllegalArgumentException("Database already exists");
//        }

        if (tenantDatabaseCache.containsKey(bankName)) {
            logger.warn("Database already exists for bank: {}", bankName);
            throw new IllegalArgumentException("Database already exists: " + bankName);
        }

        String databaseName = bankName + "_debitService";
        MongoDatabase newDatabase = mongoClient.getDatabase(databaseName);
        newDatabase.createCollection("debitCardInformation");

        logger.info("Creating bank details entry in Master DB...");
        BankDetails bankDetails = new BankDetails(bankName, databaseName);
        mongoTemplate.save(bankDetails, MASTER_DB);
        applyIndexes(databaseName);
        logger.info("Indexes applied to database: {}", databaseName);
    }

    public void saveDebitCardEntry(String bankName, DebitCard debitCard) {

        if (!tenantDatabaseCache.containsKey(bankName)) {
            logger.error("Database not found for bank: {}", bankName);
            throw new CollectionOrDbNotFoundException(bankName + " not found");
        }

        String databaseName = tenantDatabaseCache.get(bankName);
        MongoDatabase newDatabase = mongoClient.getDatabase(databaseName);
        MongoCollection<Document> collection = newDatabase.getCollection("debitCardInformation");

        Document doc = new Document()
                .append("card_network", debitCard.getCard_network())
                .append("cvv", debitCard.getCvv());

        collection.insertOne(doc);
        logger.info("Debit card entry saved successfully for bank: {}", bankName);
    }

    private void applyIndexes(String databaseName) {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        MongoCollection<Document> collection = database.getCollection("debitCardInformation");


        collection.createIndex(Indexes.ascending("card_id"), new IndexOptions().unique(true));
        collection.createIndex(Indexes.ascending("card_network"));
//        collection.createIndex(Indexes.ascending("cvv"));
        logger.info("Indexes applied successfully for database: {}", databaseName);
    }
    private void applyIndexesToAllDatabases() {
        for (String databaseName : tenantDatabaseCache.values()) {
            applyIndexes(databaseName);
        }
        logger.info("Indexing completed for all tenant databases.");
    }
}
