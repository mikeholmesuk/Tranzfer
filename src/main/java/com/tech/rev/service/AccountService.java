package com.tech.rev.service;

import com.tech.rev.dao.AccountDAO;
import com.tech.rev.exception.NoAccountFoundException;
import com.tech.rev.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AccountService {
    Logger logger = LoggerFactory.getLogger(AccountService.class);

    private AccountDAO accountDAO;

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public List<Account> listAccounts() {
        return accountDAO.listAccounts();
    }

    public Account fetchAccountbyId(Integer accountId) {
        logger.debug(String.format("Fetching `Account` by ID [%s]", accountId));

        Account account = accountDAO.fetchAccountById(accountId);

        if (account == null) {
            throw new NoAccountFoundException(
                    String.format("no account with ID of %s found", accountId));
        }

        return account;
    }

    public Account fetchByAccountNumber(String accountNumber) {
        logger.debug(String.format("Fetching `Account` with number [%s]", accountNumber));

        Account account = accountDAO.fetchAccountByAccountNo(accountNumber);

        if (account == null) {
            throw new NoAccountFoundException(
                    String.format("no account with # of %s found", accountNumber));
        }

        return account;
    }

    public Account createAccount(Account account) {
        Integer result = account.getAccountBalance() == null
                ? accountDAO.createNewAccount(account)
                : accountDAO.createNewAccountWithOpeningBalance(account);

        return this.fetchAccountbyId(result);
    }

    public Account updateAccountByAccountNo(String accountNo, Account accountData) {
        Account existingAccount = this.fetchByAccountNumber(accountNo);

        accountDAO.updateAccountHolderById(existingAccount.getId(), accountData.getAccountHolder());

        return accountDAO.fetchAccountById(existingAccount.getId());
    }
}
