package com.tech.rev.integration;

import com.tech.rev.TranzferApplication;
import com.tech.rev.TranzferConfig;
import com.tech.rev.builder.AccountBuilder;
import com.tech.rev.builder.TransferBuilder;
import com.tech.rev.model.Account;
import com.tech.rev.model.Transfer;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class TransferIntegrationTest {

    @ClassRule
    public static final DropwizardAppRule<TranzferConfig> RULE =
            new DropwizardAppRule<TranzferConfig>(TranzferApplication.class, ResourceHelpers.resourceFilePath("tranzfer_conf.yml"));

    private static Client client;

    // utility methods (would probably abstract out in a larger system)
    private Account createAccountRecord(Account accountToCreate) {
        return client.target(String.format("http://localhost:%d/account", RULE.getLocalPort()))
                .request()
                .post(Entity.entity(accountToCreate, MediaType.APPLICATION_JSON))
                .readEntity(Account.class);
    }

    private Account fetchAccountByAccountNo(String accountNumber) {
        return client.target(String.format("http://localhost:%d/account/" + accountNumber, RULE.getLocalPort()))
                .request()
                .get(Account.class);
    }

    @BeforeClass
    public static void setup() {
        client = new JerseyClientBuilder(RULE.getEnvironment()).build("test client");
    }

    @Test
    public void createAccountReturnsAccountWithIdAndAccountNumber() {
        Account accountToCreate = new AccountBuilder().withAccountNumber(null).build();

        Account createdAccount = client.target(String.format("http://localhost:%d/account", RULE.getLocalPort()))
                .request()
                .post(Entity.entity(accountToCreate, MediaType.APPLICATION_JSON))
                .readEntity(Account.class);

        assertThat(createdAccount)
                .hasFieldOrProperty("id").isNotNull()
                .hasFieldOrProperty("accountNumber").isNotNull();
    }

    @Test
    public void transferBetweenAccountsDebitsAndCreditsTransferValue() {
        Account debitAccount = createAccountRecord(
                new AccountBuilder().withAccountNumber(null).build());

        Account creditAccount = createAccountRecord(
                new AccountBuilder().withAccountNumber(null).build());

        Transfer transfer = new TransferBuilder()
                .withDebitAccountNo(debitAccount.getAccountNumber())
                .withCreditAccountNo(creditAccount.getAccountNumber())
                .withAmount(debitAccount
                        .getAccountBalance()
                        .divide(debitAccount.getAccountBalance(), 2, BigDecimal.ROUND_HALF_UP))
                .build();

        client.target(String.format("http://localhost:%d/transfer", RULE.getLocalPort()))
                .request()
                .post(Entity.entity(transfer, MediaType.APPLICATION_JSON))
                .readEntity(Transfer.class);


        assertThat(fetchAccountByAccountNo(debitAccount.getAccountNumber()).getAccountBalance())
                .isEqualTo(debitAccount.getAccountBalance().subtract(transfer.getAmount()));

        assertThat(fetchAccountByAccountNo(creditAccount.getAccountNumber()).getAccountBalance())
                .isEqualTo(creditAccount.getAccountBalance().add(transfer.getAmount()));
    }
}
