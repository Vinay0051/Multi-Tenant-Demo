package com.dcms.Multi_Tenancy_Demo.Repository;

import com.dcms.Multi_Tenancy_Demo.Model.BankDetails;
import com.dcms.Multi_Tenancy_Demo.Model.DebitCard;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MultiTenantRepository extends MongoRepository<BankDetails, Long> {
}
