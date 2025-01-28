package com.dcms.Multi_Tenancy_Demo.Controller;

import com.dcms.Multi_Tenancy_Demo.Exception.CollectionOrDbNotFoundException;
import com.dcms.Multi_Tenancy_Demo.Model.Account;
import com.dcms.Multi_Tenancy_Demo.Model.DebitCard;
import com.dcms.Multi_Tenancy_Demo.Service.MultiTenantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = Controller.class)
public class ControllerTest {

    @Mock
    private MultiTenantService multiTenantService;

    @InjectMocks
    private MultiTenantController multiTenantController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testOnBoardTenant_Success(){
        String bankName = "TestBank";
        doNothing().when(multiTenantService).createNewDatabase(bankName);

        ResponseEntity<Map<String, String>> response = multiTenantController.createDatabase(bankName);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().containsKey("message"));
        assertEquals("Database created with name: "+bankName, response.getBody().get("message"));
        verify( multiTenantService, times(1)).createNewDatabase(bankName);
    }


    @Test
    public void testOnBoardTenant_Failure() {
        String bankName = "InvalidBank";

        doThrow(new IllegalArgumentException("Invalid bank name")).when(multiTenantService).createNewDatabase(bankName);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            multiTenantController.createDatabase(bankName);
        });

        assertEquals(HttpStatus.NOT_ACCEPTABLE, exception.getStatusCode());
        assertEquals("Invalid bank name", exception.getReason());
    }

    @Test
    public void testAddDebitEntry_Success() {
        String bankName = "TestBank";
        Map<String, String> request = new HashMap<>();
        request.put("card_network", "Visa");

        doNothing().when(multiTenantService).saveDebitCardEntry(eq(bankName), any(DebitCard.class));
        ResponseEntity<Map<String, String>> response = multiTenantController.addDebitEntry(bankName, request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().containsKey("message"));
        assertEquals("Debit entry created with network: Visa", response.getBody().get("message"));
    }

    @Test
    public void testAddDebitEntry_Failure() {
        String bankName = "InvalidBank";
        Map<String, String> request = new HashMap<>();
        request.put("card_network", "Visa");

        doThrow(new CollectionOrDbNotFoundException("Database not found")).when(multiTenantService).saveDebitCardEntry(eq(bankName), any(DebitCard.class));
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            multiTenantController.addDebitEntry(bankName, request);
        });
        assertEquals(HttpStatus.NOT_ACCEPTABLE, exception.getStatusCode());
        assertEquals("Database not found", exception.getReason());
    }

    @Test
    public void testAddAccount_Success() {
        String bankName = "TestBank";
        Account account = new Account("Vinay");
        doNothing().when(multiTenantService).saveAccountEntry(eq(bankName), account);
        ResponseEntity<Map<String,String>> response = multiTenantController.addAccountEntry(bankName, account);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().containsKey("message"));
    }

    @Test
    public void testAddAccount_Failure() {
        String bankName = "InvalidBank";
        Account account = new Account("Vinay");
        doThrow(new CollectionOrDbNotFoundException("Database not found")).when(multiTenantService).saveAccountEntry(bankName, account);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            multiTenantController.addAccountEntry(bankName, account);
        });
        assertEquals(HttpStatus.NOT_ACCEPTABLE, exception.getStatusCode());
        assertEquals("Database not found", exception.getReason());
    }



//    @Test
//    public void testCreateAccount_BankAlreadyExists(){
//        String bankName = "ExistingBank";
//        String errorMessage = "Bank already exists";
//        doThrow(new IllegalArgumentException(errorMessage)).when(multiTenantService).createNewBank(bankName);
//
//        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> accountController.createBankCollection(bankName));
//
//        assertEquals(HttpStatus.NOT_ACCEPTABLE, exception.getStatusCode());
//        assertEquals(errorMessage, exception.getReason());
//    }
//
///*
//    @Test
//    public void testCreateAccount_Success(){
//        String bankName = "TestBank";
//        String customerName = "Test Customer";
//        Account account = new Account(customerName);
//
//        doReturn(account).when(multiTenantService).saveEntity(any(Account.class), eq(bankName));
//
//        Account createdAccount = accountController.createAccount(bankName, customerName);
//
//        assertNotNull(createdAccount);
//        assertEquals("Test Customer",createdAccount.getCustomerName());
//    }
//
//    @Test
//    public void testCreateAccount_CollectionNotFound() {
//        String bankName = "NonExistentBank";
//        String customerName = "John Doe";
//        String errorMessage = "NonExistentBank not found";
//
//        doThrow(new CollectionNotFoundException(errorMessage)).when(multiTenantService).saveEntity(any(Account.class), eq(bankName));
//
//        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
//            accountController.createAccount(bankName, customerName);
//        });
//
//        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
//        assertEquals(errorMessage, exception.getReason());
//    }
//*/



}
