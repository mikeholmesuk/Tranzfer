package com.tech.rev.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class Account {
    @JsonProperty("id")
    private Integer id;
    @NotNull
    @Size(min = 24, max = 24)
    @JsonProperty("account_number")
    private String accountNumber;
    @NotNull
    @Size(min = 1, max = 255)
    @JsonProperty("account_holder")
    private String accountHolder;
    @JsonProperty("account_balance")
    private BigDecimal accountBalance;
    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy-MMM-dd HH:mm:ss")
    private DateTime createdAt;

    public Account() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    @JsonProperty
    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    @JsonProperty
    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(DateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return new EqualsBuilder()
                .append(id, account.id)
                .append(accountNumber, account.accountNumber)
                .append(accountHolder, account.accountHolder)
                .append(accountBalance, account.accountBalance)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(accountNumber)
                .append(accountHolder)
                .append(accountBalance)
                .toHashCode();
    }
}
