package com.tech.rev.service;

import com.tech.rev.dao.AccountDAO;
import com.tech.rev.dao.TransferDAO;
import com.tech.rev.exception.InsufficientFundsException;
import com.tech.rev.model.Account;
import com.tech.rev.model.Transfer;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.TransactionCallback;
import org.skife.jdbi.v2.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class MoneyTransferService {
    Logger logger = LoggerFactory.getLogger(MoneyTransferService.class);

    private DBI database;
    private AccountService accountService;
    private TransferDAO transferDAO;

    public MoneyTransferService(DBI database, AccountService accountService, TransferDAO transferDAO) {
        this.database = database;
        this.accountService = accountService;
        this.transferDAO = transferDAO;
    }

    public Transfer handleMoneyTransfer(final Transfer transfer) {
        // Grab the accounts (I might refactor this out to a service)
        final Account debitAccount = accountService.fetchByAccountNumber(transfer.getDebitAccountNo());
        final Account creditAccount = accountService.fetchByAccountNumber(transfer.getCreditAccountNo());

        // Accounts exist, check there is sufficient balance
        if (debitAccount.getAccountBalance().subtract(transfer.getAmount()).compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException(String.format("Account # %s does not have  enough funds " +
                    "to make a £%s transfer", debitAccount.getAccountNumber(), transfer.getAmount()));
        };

        return database.inTransaction(
            new TransactionCallback<Transfer>() {

                public Transfer inTransaction(Handle handle, TransactionStatus transactionStatus) throws Exception {
                    AccountDAO accountDAOtx = handle.attach(AccountDAO.class);
                    TransferDAO transferDAOtx = handle.attach(TransferDAO.class);


                    Integer debitUpdated = accountDAOtx.updateAccountBalanceById(
                            debitAccount.getId(),
                            debitAccount.getAccountBalance().subtract(transfer.getAmount()));
                    Integer creditUpdated = accountDAOtx.updateAccountBalanceById(
                            creditAccount.getId(),
                            creditAccount.getAccountBalance().add(transfer.getAmount()));
                    Integer transferId = transferDAOtx.createTransfer(transfer);

                    logger.info("Created transfer: " + transferId);
                    logger.info("Got transfer back:: " + transferDAO.fetchTransferById(transferId));

                    return transfer;
                }
            }
        );
    }
}
