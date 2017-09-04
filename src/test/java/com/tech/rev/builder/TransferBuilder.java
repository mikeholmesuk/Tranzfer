package com.tech.rev.builder;

import com.google.common.collect.Range;
import com.tech.rev.model.Transfer;
import org.joda.time.DateTime;
import uk.org.fyodor.generators.RDG;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.UUID;

public class TransferBuilder {
    private Integer id;
    private String debitAccountNo = UUID.randomUUID().toString();
    private String creditAccountNo = UUID.randomUUID().toString();
    private BigDecimal amount = RDG.bigDecimal().next();
    private String currency = "GBP";
    private DateTime createdAt;

    public TransferBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public TransferBuilder withDebitAccountNo(String debitAccountNo) {
        this.debitAccountNo = debitAccountNo;
        return this;
    }

    public TransferBuilder withCreditAccountNo(String creditAccountNo) {
        this.creditAccountNo = creditAccountNo;
        return this;
    }

    public TransferBuilder withAmount(BigDecimal amount) {
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        return this;
    }

    public TransferBuilder withCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public TransferBuilder withCReatedAt(DateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Transfer build() {
        Transfer transfer = new Transfer();

        transfer.setId(this.id);
        transfer.setDebitAccountNo(this.debitAccountNo);
        transfer.setCreditAccountNo(this.creditAccountNo);
        transfer.setAmount(this.amount);
        transfer.setCurrency(this.currency);
        transfer.setCreatedAt(this.createdAt);

        return transfer;
    }
}
