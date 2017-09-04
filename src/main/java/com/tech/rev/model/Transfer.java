package com.tech.rev.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class Transfer {
    @JsonProperty("id")
    private Integer id;
    @NotNull
    @Size(min = 36, max = 36)
    @JsonProperty("debit_account_no")
    private String debitAccountNo;
    @NotNull
    @Size(min = 36, max = 36)
    @JsonProperty("credit_account_no")
    private String creditAccountNo;
    @NotNull
    private BigDecimal amount;
    private String currency;
    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy-MMM-dd HH:mm:ss")
    private DateTime createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDebitAccountNo() {
        return debitAccountNo;
    }

    @JsonProperty
    public void setDebitAccountNo(String debitAccountNo) {
        this.debitAccountNo = debitAccountNo;
    }

    public String getCreditAccountNo() {
        return creditAccountNo;
    }

    @JsonProperty
    public void setCreditAccountNo(String creditAccountNo) {
        this.creditAccountNo = creditAccountNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @JsonProperty
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    @JsonProperty
    public void setCurrency(String currency) {
        this.currency = currency;
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

        Transfer transfer = (Transfer) o;

        return new EqualsBuilder()
                .append(id, transfer.id)
                .append(debitAccountNo, transfer.debitAccountNo)
                .append(creditAccountNo, transfer.creditAccountNo)
                .append(amount, transfer.amount)
                .append(currency, transfer.currency)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(debitAccountNo)
                .append(creditAccountNo)
                .append(amount)
                .append(currency)
                .toHashCode();
    }
}
