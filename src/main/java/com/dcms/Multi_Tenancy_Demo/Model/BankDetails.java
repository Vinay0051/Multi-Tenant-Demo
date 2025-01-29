package com.dcms.Multi_Tenancy_Demo.Model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "BankDetails")
public class BankDetails {

    private String bankName;
    private String databaseName;

    public BankDetails(String bankName, String databaseName) {
        this.bankName = bankName;
        this.databaseName = databaseName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
}
