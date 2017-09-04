package com.tech.rev.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.rev.builder.AccountBuilder;
import io.dropwizard.jackson.Jackson;
import org.joda.time.DateTime;
import org.junit.Test;

import java.math.BigDecimal;

import static io.dropwizard.testing.FixtureHelpers.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AccountTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Test
    public void serializesToJSON() throws Exception {
        Account account = new AccountBuilder()
                .withId(2)
                .withAccountNumber("327fcd0e-d336-4780-8d69-ab60ded7c99c")
                .withAccountHolder("Mike Holmes Esq.")
                .withAccountBalance(new BigDecimal(999.95))
                .withcreatedAt(new DateTime(2017, 9, 03, 13, 16, 03))
                .build();

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/account.json"), Account.class));

        assertThat(MAPPER.writeValueAsString(account)).isEqualTo(expected);
    }

    @Test
    public void deserializesFromJSON() throws Exception {
        final Account account = new AccountBuilder()
                .withId(2)
                .withAccountNumber("327fcd0e-d336-4780-8d69-ab60ded7c99c")
                .withAccountHolder("Mike Holmes Esq.")
                .withAccountBalance(new BigDecimal(999.95))
                .withcreatedAt(new DateTime(2017, 9, 03, 13, 16, 03))
                .build();

        assertThat(MAPPER.readValue(fixture("fixtures/account.json"), Account.class))
                .isEqualTo(account);
    }
}