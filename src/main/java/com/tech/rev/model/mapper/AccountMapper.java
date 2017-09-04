package com.tech.rev.model.mapper;

import com.tech.rev.model.Account;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountMapper implements ResultSetMapper<Account> {
    public Account map(int index, ResultSet resultSet, StatementContext statementContext)
        throws SQLException {

        Account account = new Account();
        account.setId(resultSet.getInt("id"));
        account.setAccountNumber(resultSet.getString("account_number"));
        account.setAccountHolder(resultSet.getString("account_holder"));
        account.setAccountBalance(resultSet.getBigDecimal("account_balance"));
        account.setCreatedAt(
                new DateTime(resultSet.getTimestamp("created_at"))
                        .withZone(DateTimeZone.UTC));

        return account;
    }
}
