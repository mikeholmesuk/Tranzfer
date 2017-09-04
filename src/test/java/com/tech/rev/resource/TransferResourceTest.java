package com.tech.rev.resource;

import com.tech.rev.builder.TransferBuilder;
import com.tech.rev.dao.TransferDAO;
import com.tech.rev.exception.WebExceptionMapper;
import com.tech.rev.model.Transfer;
import com.tech.rev.service.MoneyTransferService;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import uk.org.fyodor.generators.RDG;
import uk.org.fyodor.range.Range;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TransferResourceTest {
    private static TransferDAO transferDAO = mock(TransferDAO.class);
    private static MoneyTransferService moneyTransferSvc = mock(MoneyTransferService.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new TransferResource(transferDAO, moneyTransferSvc))
            .addResource(new WebExceptionMapper())
            .build();

    @After
    public void tearDown() {
        reset(transferDAO);
        reset(moneyTransferSvc);
    }

    // Response codes testing
    @Test
    public void getTransferListReturnsHttp200() {
        List<Transfer> transferList = new ArrayList();
        transferList.add(new TransferBuilder().build());

        when(transferDAO.listTransfers()).thenReturn(transferList);

        assertThat(resources.client().target("/transfer")
                .request()
                .get()
                .getStatus()).isEqualTo(200);
    }

    @Test
    public void handleMoneyTransferReturnsHttp201() {
        Transfer transfer = new TransferBuilder().build();

        when(moneyTransferSvc.handleMoneyTransfer(ArgumentMatchers.isA(Transfer.class))).thenReturn(transfer);

        assertThat(resources.client().target("/transfer")
                .request()
                .post(Entity.entity(transfer, MediaType.APPLICATION_JSON))
                .getStatus()).isEqualTo(201);
    }

    @Test
    public void getValidTransferReturnsHttp200() {
        Transfer transfer = new TransferBuilder().withId(RDG.integer().next()).build();

        when(transferDAO.fetchTransferById(ArgumentMatchers.eq(transfer.getId()))).thenReturn(transfer);

        assertThat(resources.client().target("/transfer/" + transfer.getId())
                .request()
                .get()
                .getStatus()).isEqualTo(200);
    }

    // Validate expected call is made
    @Test
    public void getAccountListCallsListAccountsOnly() {
        List<Transfer> transferList = new ArrayList();
        transferList.add(new TransferBuilder().build());

        when(transferDAO.listTransfers()).thenReturn(transferList);

        resources.client().target("/transfer").request().get();

        verify(transferDAO, only()).listTransfers();
    }

    @Test
    public void createAccountCallsCreateAccountOnly() {
        Transfer transfer = new TransferBuilder().build();

        when(moneyTransferSvc.handleMoneyTransfer(ArgumentMatchers.any(Transfer.class))).thenReturn(transfer);

        resources.client().target("/transfer")
                .request()
                .post(Entity.entity(transfer, MediaType.APPLICATION_JSON));

        verify(moneyTransferSvc, only()).handleMoneyTransfer(ArgumentMatchers.eq(transfer));
    }

    @Test
    public void getValidAccountCallsFetchByAccountOnly() {
        Transfer transfer = new TransferBuilder()
                .withId(RDG.integer(Range.closed(1, Integer.MAX_VALUE)).next()).build();

        when(transferDAO.fetchTransferById(ArgumentMatchers.eq(transfer.getId()))).thenReturn(transfer);

        resources.client().target("/transfer/" + transfer.getId()).request().get();

        verify(transferDAO, only())
                .fetchTransferById(ArgumentMatchers.eq(transfer.getId()));
    }

}