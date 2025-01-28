import com.dcms.Multi_Tenancy_Demo.Controller.AccountController;
import com.dcms.Multi_Tenancy_Demo.Exception.CollectionNotFoundException;
import com.dcms.Multi_Tenancy_Demo.Model.Account;
import com.dcms.Multi_Tenancy_Demo.Service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = Controller.class)
public class ControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateBankCollection_Success(){
        String bankName = "TestBank";
        doNothing().when(accountService).createNewBank(bankName);

        String response = accountController.createBankCollection(bankName);
        assertEquals("Bank Collection created", response);
    }


    @Test
    public void testCreateAccount_BankAlreadyExists(){
        String bankName = "ExistingBank";
        String errorMessage = "Bank already exists";
        doThrow(new IllegalArgumentException(errorMessage)).when(accountService).createNewBank(bankName);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> accountController.createBankCollection(bankName));

        assertEquals(HttpStatus.NOT_ACCEPTABLE, exception.getStatusCode());
        assertEquals(errorMessage, exception.getReason());
    }

    @Test
    public void testCreateAccount_Success(){
        String bankName = "TestBank";
        String customerName = "Test Customer";
        Account account = new Account(customerName);

        doReturn(account).when(accountService).saveEntity(any(Account.class), eq(bankName));

        Account createdAccount = accountController.createAccount(bankName, customerName);

        assertNotNull(createdAccount);
        assertEquals("Test Customer",createdAccount.getCustomerName());
    }

    @Test
    public void testCreateAccount_CollectionNotFound() {
        String bankName = "NonExistentBank";
        String customerName = "John Doe";
        String errorMessage = "NonExistentBank not found";

        doThrow(new CollectionNotFoundException(errorMessage)).when(accountService).saveEntity(any(Account.class), eq(bankName));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            accountController.createAccount(bankName, customerName);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals(errorMessage, exception.getReason());
    }



}
