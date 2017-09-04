package com.tech.rev.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.tech.rev.builder.AccountBuilder;
import com.tech.rev.exception.NoAccountFoundException;
import com.tech.rev.exception.WebExceptionMapper;
import com.tech.rev.model.Account;
import com.tech.rev.service.AccountService;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import uk.org.fyodor.generators.RDG;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

public class AccountResourceTest {
    private static final AccountService accountService = mock(AccountService.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new AccountResource(accountService))
            .addResource(new WebExceptionMapper())
            .build();

    @After
    public void tearDown() {
        reset(accountService);
    }

    // Response codes testing
    @Test
    public void getAccountListReturnsHttp200() {
        List<Account> accountsList = new ArrayList();
        accountsList.add(new AccountBuilder().build());

        when(accountService.listAccounts()).thenReturn(accountsList);

        assertThat(resources.client().target("/account")
                .request()
                .get()
                .getStatus()).isEqualTo(200);
    }

    @Test
    public void createAccountReturnsHttp201() {
        Account account = new AccountBuilder().build();
        when(accountService.createAccount(ArgumentMatchers.any(Account.class))).thenReturn(account);

        assertThat(resources.client().target("/account")
                .request()
                .post(Entity.entity(account, MediaType.APPLICATION_JSON))
                .getStatus()).isEqualTo(201);
    }

    @Test
    public void getValidAccountReturnsHttp200() {
        Account account = new AccountBuilder().build();
        when(accountService.fetchByAccountNumber(
                ArgumentMatchers.same(account.getAccountNumber()))).thenReturn(account);

        assertThat(resources.client().target("/account/" + account.getAccountNumber())
                .request()
                .get()
                .getStatus()).isEqualTo(200);
    }

    @Test
    public void getInvalidAccountReturnsHttp404() {
        String searchString = "foo";

        when(accountService.fetchByAccountNumber(
                ArgumentMatchers.eq(searchString))).thenThrow(new NoAccountFoundException("No account with this number"));

        assertThat(resources.client().target("/account/" + searchString)
                .request()
                .get()
                .getStatus()).isEqualTo(404);
    }

    @Test
    public void updateAccountReturnsHttp201() {
        Account account = new AccountBuilder().build();
        Account updatedAccount = new AccountBuilder()
                .withAccountHolder(account.getAccountHolder())
                .withAccountNumber(account.getAccountNumber())
                .build();

        when(accountService.updateAccountByAccountNo(
                ArgumentMatchers.eq(account.getAccountNumber()), ArgumentMatchers.isA(Account.class)))
                .thenReturn(updatedAccount);

        assertThat(resources.client().target("/account/" + account.getAccountNumber())
            .request()
            .put(Entity.entity(account, MediaType.APPLICATION_JSON))
            .getStatus()).isEqualTo(201);
    }

    // Validate that the service is called
    @Test
    public void getAccountListCallsListAccountsOnly() {
        List<Account> accountsList = new ArrayList();
        accountsList.add(new AccountBuilder().build());

        when(accountService.listAccounts()).thenReturn(accountsList);

        resources.client().target("/account").request().get();

        verify(accountService, only()).listAccounts();
    }

    @Test
    public void createAccountCallsCreateAccountOnly() {
        Account account = new AccountBuilder().build();
        when(accountService.createAccount(ArgumentMatchers.any(Account.class))).thenReturn(account);

        resources.client().target("/account")
                .request()
                .post(Entity.entity(account, MediaType.APPLICATION_JSON));

        verify(accountService, only()).createAccount(ArgumentMatchers.eq(account));
    }

    @Test
    public void getValidAccountCallsFetchByAccountOnly() {
        Account account = new AccountBuilder().build();
        when(accountService.fetchByAccountNumber(
                ArgumentMatchers.same(account.getAccountNumber()))).thenReturn(account);

        resources.client().target("/account/" + account.getAccountNumber()).request().get();

        verify(accountService, only())
                .fetchByAccountNumber(ArgumentMatchers.eq(account.getAccountNumber()));
    }


    @Test
    public void updateAccountCallsUpdateAccountOnly() {
        Account account = new AccountBuilder().build();
        Account updatedAccount = new AccountBuilder()
                .withAccountHolder(account.getAccountHolder())
                .withAccountNumber(account.getAccountNumber())
                .build();

        when(accountService.updateAccountByAccountNo(
                ArgumentMatchers.eq(account.getAccountNumber()), ArgumentMatchers.isA(Account.class)))
                .thenReturn(updatedAccount);

        resources.client().target("/account/" + account.getAccountNumber())
                .request()
                .put(Entity.entity(account, MediaType.APPLICATION_JSON));

        verify(accountService, only())
                .updateAccountByAccountNo(
                        ArgumentMatchers.eq(account.getAccountNumber()),
                        ArgumentMatchers.any(Account.class));
    }
}