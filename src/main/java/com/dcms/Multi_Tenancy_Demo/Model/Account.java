package com.dcms.Multi_Tenancy_Demo.Model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "banks")
@Data

public class Account {

    @Id
    private String bank_id;

    private String account_holder_name;

    public Account(String account_holder_name) {
        this.account_holder_name = account_holder_name;
    }

    public String getCustomerName() {
        return account_holder_name;
    }
}
