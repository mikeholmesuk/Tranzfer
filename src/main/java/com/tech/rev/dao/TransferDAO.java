package com.tech.rev.dao;

import com.tech.rev.model.Transfer;
import com.tech.rev.model.mapper.TransferMapper;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(TransferMapper.class)
public interface TransferDAO {

    @SqlQuery("SELECT * FROM transfers")
    List<Transfer> listTransfers();

    @SqlQuery("SELECT * FROM transfers WHERE id = :id")
    Transfer fetchTransferById(@Bind("id") Integer id);

    @SqlQuery("SELECT * FROM transfers WHERE debit_account_no = :debit_account_no")
    List<Transfer> listTransfersByDebitAccountNo(@Bind("debit_account_no") String debitAccountNo);

    @SqlQuery("SELECT * FROM transfers WHERE credit_account_no = :credit_account_no")
    List<Transfer> listTransfersByCreditAccountNo(@Bind("credit_account_no") String creditAccountNo);

    @SqlQuery("SELECT * FROM transfers WHERE debit_account_no = :account_no OR credit_account_no = :account_no")
    List<Transfer> listAllTransfersByAccountNo(@Bind("account_no") String accountNo);

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO transfers " +
            "(debit_account_no, credit_account_no, amount, currency) " +
            "VALUES (:debitAccountNo, :creditAccountNo, :amount, :currency)")
    Integer createTransfer(@BindBean Transfer transfer);
}
