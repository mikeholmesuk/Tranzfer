package com.tech.rev.builder;

import com.google.common.collect.Range;
import com.tech.rev.model.Account;
import org.joda.time.DateTime;
import uk.org.fyodor.generators.RDG;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class AccountBuilder {
    private Integer id;
    private String accountNumber = UUID.randomUUID().toString();
    private String accountHolder = RDG.string().next();
    private BigDecimal accountBalance = RDG.bigDecimal(Double.MAX_VALUE).next();
    private DateTime createdAt;

    public AccountBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public AccountBuilder withAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public AccountBuilder withAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
        return this;
    }

    public AccountBuilder withAccountBalance(BigDecimal accountBalance) {
        if (accountBalance != null) {
            this.accountBalance = accountBalance.setScale(2, RoundingMode.HALF_UP);
        } else {
            this.accountBalance = accountBalance;
        }
        return this;
    }

    public AccountBuilder withcreatedAt(DateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Account build() {
        Account account = new Account();

        account.setId(this.id);
        account.setAccountNumber(this.accountNumber);
        account.setAccountHolder(this.accountHolder);
        account.setAccountBalance(this.accountBalance);
        account.setCreatedAt(this.createdAt);

        return account;
    }
}
