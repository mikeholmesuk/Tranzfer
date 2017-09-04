package com.tech.rev.resource;

import com.tech.rev.dao.TransferDAO;
import com.tech.rev.model.Transfer;
import com.tech.rev.service.MoneyTransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/transfer")
@Produces(MediaType.APPLICATION_JSON)
public class TransferResource {
    Logger logger = LoggerFactory.getLogger(TransferResource.class);

    private TransferDAO transferDAO;
    private MoneyTransferService moneyTransferService;

    public TransferResource(TransferDAO transferDAO, MoneyTransferService moneyTransferService) {
        this.transferDAO = transferDAO;
        this.moneyTransferService = moneyTransferService;
    }

    @GET
    public Response getTransferList() {
        logger.debug("Listing all transfers...");

        return Response.ok(transferDAO.listTransfers()).build();
    }

    @POST
    public Response createTransfer(Transfer transfer) {
        logger.debug("Creating new Transfer between accounts %s and %s", transfer.getDebitAccountNo(), transfer.getCreditAccountNo());


        Transfer createdTransfer = moneyTransferService.handleMoneyTransfer(transfer);

        return Response
                .status(Response.Status.CREATED)
                .entity(createdTransfer)
                .build();
    }

    @GET
    @Path("/{transfer_id}")
    public Response getTransferById(@PathParam("transfer_id") Integer transferId) {
        logger.debug(String.format("Returning transfer by id %s", transferId));

        return Response.ok(transferDAO.fetchTransferById(transferId)).build();
    }
}
