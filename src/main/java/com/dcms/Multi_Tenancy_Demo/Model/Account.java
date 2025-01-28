package com.dcms.Multi_Tenancy_Demo.Model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



public class Account {
    @Id
    private String bank_id;

    private String account_holder_name;

    public Account(String account_holder_name) {
        this.account_holder_name = account_holder_name;
    }

    public String getAccount_holder_name() {
        return account_holder_name;
    }

    public String getBank_id() {
        return bank_id;
    }

    public void setBank_id(String bank_id) {
        this.bank_id = bank_id;
    }

    public void setAccount_holder_name(String account_holder_name) {
        this.account_holder_name = account_holder_name;
    }


}
