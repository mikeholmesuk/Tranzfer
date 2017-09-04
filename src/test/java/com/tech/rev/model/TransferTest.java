package com.tech.rev.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.rev.builder.AccountBuilder;
import com.tech.rev.builder.TransferBuilder;
import io.dropwizard.jackson.Jackson;
import org.joda.time.DateTime;
import org.junit.Test;

import java.math.BigDecimal;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class TransferTest {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Test
    public void serializesToJSON() throws Exception {

        final Transfer transfer = new TransferBuilder()
                .withId(1)
                .withAmount(new BigDecimal(100.56))
                .withDebitAccountNo("1712ba7e-93f5-425c-a7b0-8ca53453f4f0")
                .withCreditAccountNo("e9f28972-5075-414e-926e-c4d8ee4124f6")
                .withCReatedAt(new DateTime(2017, 9, 03, 15, 27, 44))
                .build();

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/transfer.json"), Transfer.class));

        assertThat(MAPPER.writeValueAsString(transfer)).isEqualTo(expected);
    }

    @Test
    public void deserializesFromJSON() throws Exception {

        final Transfer transfer = new TransferBuilder()
                .withId(1)
                .withAmount(new BigDecimal(100.56))
                .withDebitAccountNo("1712ba7e-93f5-425c-a7b0-8ca53453f4f0")
                .withCreditAccountNo("e9f28972-5075-414e-926e-c4d8ee4124f6")
                .withCReatedAt(new DateTime(2017, 9, 03, 15, 27, 44))
                .build();

        assertThat(MAPPER.readValue(fixture("fixtures/transfer.json"), Transfer.class))
                .isEqualTo(transfer);
    }
}