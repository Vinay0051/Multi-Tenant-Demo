package com.dcms.Multi_Tenancy_Demo.Service;

import com.dcms.Multi_Tenancy_Demo.Exception.CollectionNotFoundException;
import com.dcms.Multi_Tenancy_Demo.Model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountServiceTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateNewBank_Success() {
        String bankName = "BankOfTest";
        Mockito.doReturn(false).when(mongoTemplate).collectionExists(bankName);

        accountService.createNewBank(bankName);

        Mockito.verify(mongoTemplate, Mockito.times(1)).createCollection(bankName);
    }

    @Test
    void testCreateNewBank_BankAlreadyExists() {

        String bankName = "ExistingBank";
        Mockito.doReturn(true).when(mongoTemplate).collectionExists(bankName);

        assertThrows(IllegalArgumentException.class, () -> accountService.createNewBank(bankName));
        Mockito.verify(mongoTemplate, Mockito.never()).createCollection(bankName);
    }

    @Test
    void testSaveEntity_Success() {
        String bankName = "BankOfTest";
        Account account = new Account(bankName);
        Mockito.doReturn(true).when(mongoTemplate).collectionExists(bankName);
        Mockito.doReturn(account).when(mongoTemplate).save(account, bankName);

        Account savedAccount = accountService.saveEntity(account, bankName);
        assertNotNull(savedAccount);
        Mockito.verify(mongoTemplate, Mockito.times(1)).save(account, bankName);
    }

    @Test
    void testSaveEntity_CollectionNotFound() {

        String bankName = "NonExistentBank";
        Account account = new Account(bankName);
        Mockito.doReturn(false).when(mongoTemplate).collectionExists(bankName);

        assertThrows(CollectionNotFoundException.class, () -> accountService.saveEntity(account, bankName));
        Mockito.verify(mongoTemplate, Mockito.never()).save(account, bankName);
    }
}