package com.dcms.Multi_Tenancy_Demo.Repository;

import com.dcms.Multi_Tenancy_Demo.Model.Account;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountRepository extends MongoRepository<Account, Long> {

}
