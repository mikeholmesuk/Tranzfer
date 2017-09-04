package com.tech.rev.service;

import com.tech.rev.builder.AccountBuilder;
import com.tech.rev.dao.AccountDAO;
import com.tech.rev.exception.NoAccountFoundException;
import com.tech.rev.model.Account;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import uk.org.fyodor.generators.RDG;
import uk.org.fyodor.range.Range;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AccountServiceTest {

    private static final AccountDAO accountDAO = mock(AccountDAO.class);
    private AccountService accountService;

    @Before
    public void setup() {
        this.accountService = new AccountService(accountDAO);
    }

    @After
    public void tearDown() {
        reset(accountDAO);
    }

    @Test
    public void listAccountsCallsListAccountsOnly() {
        List<Account> accountList = new ArrayList<Account>();
        accountList.add(new AccountBuilder().build());

        when(accountDAO.listAccounts()).thenReturn(accountList);

        List<Account> results = accountService.listAccounts();

        verify(accountDAO, only()).listAccounts();
    }

    @Test
    public void fetchAccountByIdReturnsExpectedAccount() {
        Account account = new AccountBuilder()
                .withId(RDG.integer(Range.closed(1, Integer.MAX_VALUE)).next()).build();

        when(accountDAO.fetchAccountById(ArgumentMatchers.eq(account.getId()))).thenReturn(account);

        Account result = accountService.fetchAccountbyId(account.getId());

        assertThat(result).isEqualTo(account);
    }

    @Test(expected = NoAccountFoundException.class)
    public void fetchByIdThrowsNoAccountFoundIfAccountIsNull() {
        Integer invalidId = RDG.integer(Range.closed(1, Integer.MAX_VALUE)).next();

        when(accountDAO.fetchAccountById(invalidId)).thenReturn(null);

        // Throws exception
        accountService.fetchAccountbyId(invalidId);
    }

    @Test
    public void fetchAccountByAccountNumberReturnsExpectedAccount() {
        Account account = new AccountBuilder().build();

        when(accountDAO.fetchAccountByAccountNo(ArgumentMatchers.eq(account.getAccountNumber()))).thenReturn(account);

        Account result = accountService.fetchByAccountNumber(account.getAccountNumber());

        assertThat(result).isEqualTo(account);
    }

    @Test(expected = NoAccountFoundException.class)
    public void fetchByAccountNumberThrowsNoAccountFoundIfAccountIsNull() {
        String invalidUUID = UUID.randomUUID().toString();

        when(accountDAO.fetchAccountByAccountNo(invalidUUID)).thenReturn(null);

        // Throws exception
        accountService.fetchByAccountNumber(invalidUUID);
    }

    @Test
    public void createWithNoAccountBalanceCallsCreateNewAccount() {
        Account accountToCreate = new AccountBuilder()
                .withAccountHolder(RDG.string().next())
                .withAccountBalance(null)
                .build();
        Integer randomInt = RDG.integer().next();

        when(accountDAO.createNewAccount(ArgumentMatchers.isA(Account.class))).thenReturn(randomInt);
        when(accountDAO.fetchAccountById(ArgumentMatchers.eq(randomInt))).thenReturn(accountToCreate);

        accountService.createAccount(accountToCreate);

        verify(accountDAO, times(1)).createNewAccount(ArgumentMatchers.eq(accountToCreate));
    }

    @Test
    public void createWithAccountBalanceCallsCreateNewAccountWithBalance() {
        Account accountToCreate = new AccountBuilder()
                .withAccountHolder(RDG.string().next())
                .withAccountBalance(RDG.bigDecimal().next())
                .build();
        Integer randomInt = RDG.integer().next();

        when(accountDAO.createNewAccountWithOpeningBalance(ArgumentMatchers.isA(Account.class))).thenReturn(randomInt);
        when(accountDAO.fetchAccountById(randomInt)).thenReturn(accountToCreate);

        accountService.createAccount(accountToCreate);

        verify(accountDAO, times(1)).createNewAccountWithOpeningBalance(ArgumentMatchers.eq(accountToCreate));
    }
}
