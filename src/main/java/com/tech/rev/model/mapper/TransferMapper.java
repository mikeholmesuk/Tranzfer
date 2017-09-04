package com.tech.rev.model.mapper;

import com.tech.rev.model.Transfer;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransferMapper implements ResultSetMapper<Transfer> {
    public Transfer map(int index, ResultSet resultSet, StatementContext statementContext)
        throws SQLException {

        Transfer transfer = new Transfer();

        transfer.setId(resultSet.getInt("id"));
        transfer.setDebitAccountNo(resultSet.getString("debit_account_no"));
        transfer.setCreditAccountNo(resultSet.getString("credit_account_no"));
        transfer.setAmount(resultSet.getBigDecimal("amount"));
        transfer.setCurrency(resultSet.getString("currency"));
        transfer.setCreatedAt(
                new DateTime(resultSet.getTimestamp("created_at"))
                        .withZone(DateTimeZone.UTC));

        return transfer;
    }
}
