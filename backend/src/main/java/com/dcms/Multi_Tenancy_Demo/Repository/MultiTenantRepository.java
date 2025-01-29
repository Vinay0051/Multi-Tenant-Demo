package com.dcms.Multi_Tenancy_Demo.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MultiTenantRepository extends MongoRepository<Account, Long> {

}
