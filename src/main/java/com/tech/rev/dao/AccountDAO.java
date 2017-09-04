package com.tech.rev.dao;

import com.tech.rev.model.Account;
import com.tech.rev.model.mapper.AccountMapper;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.math.BigDecimal;
import java.util.List;

@RegisterMapper(AccountMapper.class)
public interface AccountDAO {

    @SqlQuery("SELECT * FROM accounts")
    List<Account> listAccounts();

    @SqlQuery("SELECT * FROM accounts where id = :id")
    Account fetchAccountById(@Bind("id") Integer accountId);

    @SqlQuery("SELECT * FROM accounts where account_number = :account_number")
    Account fetchAccountByAccountNo(@Bind("account_number") String accountNumber);

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO accounts (account_holder) VALUES (:accountHolder)")
    Integer createNewAccount(@BindBean Account account);

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO accounts (account_holder, account_balance) VALUES (:accountHolder, :accountBalance)")
    Integer createNewAccountWithOpeningBalance(@BindBean Account account);

    @SqlUpdate("UPDATE accounsts SET account_holder ")
    Integer updateAccountHolderById(@Bind("account_id") Integer accountId, @Bind("account_holder") String accountHolder);

    @SqlUpdate("UPDATE accounts SET account_balance = :account_balance WHERE id = :account_id")
    Integer updateAccountBalanceById(@Bind("account_id") Integer accountId, @Bind("account_balance") BigDecimal accountBalance);
}
